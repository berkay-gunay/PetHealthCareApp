package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityPetPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PetPage extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private Intent intent;


    private ActivityPetPageBinding binding;
    private String enteredID;
    private String userID;
    private String petName;
    private String kindOfPet;
    private String infOfPet;
    private String emailOfPetOwner;
    private PetInfo petInfo;

    ArrayList<PetInfo> petInfoArrayList;
    PetInfoAdapter petInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        petInfoArrayList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        intent = getIntent();

        enteredID = intent.getStringExtra("keyID");


        getdata();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(PetPage.this));
        petInfoAdapter = new PetInfoAdapter(petInfoArrayList);
        binding.recyclerView.setAdapter(petInfoAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //int permission = 0;
        Intent intent1 = getIntent();
        int permission = intent1.getIntExtra("permission",0);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(item.getItemId() == R.id.VaccationScheduleItem){
            Intent intent4 = new Intent(PetPage.this, PetPageVaccine.class);
            intent4.putExtra("petID",enteredID);
            intent4.putExtra("permission",permission);
            startActivity(intent4);
        }
        if(item.getItemId() == R.id.PetInspectionItem){

            Intent intent2 = new Intent(PetPage.this, PetInspection.class);
            intent2.putExtra("petid",enteredID);
            intent2.putExtra("permission",permission);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            //finish();


        }else if (item.getItemId() == R.id.DeleteRecordItem){

            if(permission == 1){

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseFirestore.collection("PetRecordings").document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PetPage.this, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                //petInfoArrayList.remove(petInfo);
                                /*Intent intent3 = new Intent(PetPage.this, VetPage.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent3);*/
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PetPage.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }else{
                Toast.makeText(this, "You don't have a permission.", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void getdata(){

        /*Intent intent = getIntent();
        enteredID = intent.getStringExtra("keyID");*/

        firebaseFirestore.collection("PetRecordings").whereEqualTo("PetID",enteredID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(PetPage.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        petName = (String)documentSnapshot.getData().get("PetName");
                        userID = (String)documentSnapshot.getData().get("PetID");
                        kindOfPet = (String)documentSnapshot.getData().get("KindOfThisPet");
                        infOfPet = (String) documentSnapshot.getData().get("Information");
                        Timestamp dateOfLastUpdate = (Timestamp) documentSnapshot.getData().get("Date");
                        emailOfPetOwner = (String) documentSnapshot.getData().get("EmailOfPetOwner");

                        petInfo = new PetInfo(petName,userID,kindOfPet,infOfPet,dateOfLastUpdate,emailOfPetOwner);
                        petInfoArrayList.add(petInfo);

                    }
                    petInfoAdapter.notifyDataSetChanged();
                }
            }
        });
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}