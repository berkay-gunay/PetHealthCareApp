package com.berkaygunay.finalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVetProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class VetProfile extends AppCompatActivity {

    ActivityVetProfileBinding binding;
    private FirebaseFirestore firebaseFirestore;
    public String vetEmail,nameSurname,phoneNumber,address,district,city,clinicName,imageURL,getEmailPetOwner;
    public int permission = 0,count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        vetEmail = intent.getStringExtra("emaill");
        Intent intent1 = getIntent();
        permission = intent1.getIntExtra("permission",0);
        getEmailPetOwner = intent1.getStringExtra("emailofpetowner");
        if(permission == 0){
            binding.addCommentButton.setVisibility(View.INVISIBLE);
        }
        getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vet_comment_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.showComments){
            Intent goToVetComments = new Intent(VetProfile.this, VetComments.class);
            goToVetComments.putExtra("vetEmail",vetEmail);
            startActivity(goToVetComments);
        }
        if(item.getItemId() == R.id.deleteComment){
            if(permission == 1){
                firebaseFirestore.collection("VetUsers").document(vetEmail).collection("Comments").document(getEmailPetOwner).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VetProfile.this, "Your comment is deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VetProfile.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "you don't have permission", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(){


        firebaseFirestore.collection("VetUsers").document(vetEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        nameSurname = (String) documentSnapshot.getData().get("NameSurname");
                        clinicName = (String) documentSnapshot.getData().get("ClinicName");
                        phoneNumber = (String) documentSnapshot.getData().get("PhoneNumber");
                        address = (String) documentSnapshot.getData().get("Address");
                        district = (String) documentSnapshot.getData().get("District");
                        city = (String) documentSnapshot.getData().get("City");
                        imageURL = (String) documentSnapshot.getData().get("ImageURL");
                        binding.nameSurnameTextView.setText(nameSurname);
                        binding.clinicNameTextView.setText(clinicName);
                        binding.emailTextView.setText(vetEmail);
                        binding.phoneNumberTextView.setText(phoneNumber);
                        binding.addressTextView.setText(address);
                        binding.districtCityTextView.setText(district + " / " + city);
                        //binding.imageView.setImageDrawable((getDrawable(R.drawable.selectimageicon)));
                        Picasso.get().load(imageURL).into(binding.imageView);
                    }else{
                        Toast.makeText(VetProfile.this, "no such document", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VetProfile.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void clickedAddCommentButton(View view){

        firebaseFirestore.collection("PetRecordings").whereEqualTo("EmailOfVet",vetEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String emailofPetOwner = (String) documentSnapshot.getData().get("EmailOfPetOwner");
                        if(getEmailPetOwner.equals(emailofPetOwner)){
                            count++;
                        }
                    }
                    if(count != 0){
                        AlertDialog.Builder alert = new AlertDialog.Builder(VetProfile.this);
                        EditText editText = new EditText(VetProfile.this);
                        editText.setSingleLine(false);
                        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                        alert.setTitle("Write Comment");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String getComment = editText.getText().toString();
                                Map<String,String> data = new HashMap<>();
                                data.put("Comment",getComment);
                                firebaseFirestore.collection("VetUsers").document(vetEmail).collection("Comments").document(getEmailPetOwner).set(data);
                                firebaseFirestore.collection("PetOwnerUsers").document(getEmailPetOwner).collection("PetOwnerComments").document(vetEmail).set(data);

                            }
                        });
                        alert.setView(editText);
                        alert.show();
                    }else{
                        Toast.makeText(VetProfile.this, "You can't write comment for this Vet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VetProfile.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}