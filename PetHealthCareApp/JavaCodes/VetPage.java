package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVetPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class VetPage extends AppCompatActivity {

    ActivityVetPageBinding binding;
    private ArrayList<String> commentArrayList;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String enterid, id,email;
    CommentAdapter commentAdapter;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        commentArrayList = new ArrayList<>();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getData();

        binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(VetPage.this));
        commentAdapter = new CommentAdapter(commentArrayList);
        binding.commentRecyclerView.setAdapter(commentAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*Intent intent = getIntent();
        String email = intent.getStringExtra("email");*/

        if(item.getItemId() == R.id.MyProfileItem){
            Intent intent1 = new Intent(VetPage.this,VetProfile.class);
            intent1.putExtra("emaill",email);
            startActivity(intent1);

        }
        if(item.getItemId() == R.id.UploadImageItem){
            Intent intentToUploadImage = new Intent(VetPage.this,VetUploadImage.class);
            intentToUploadImage.putExtra("emailtoupload",email);
            startActivity(intentToUploadImage);
            //finish();

        }
        if(item.getItemId() == R.id.SignOutItem){
            auth.signOut();
            Intent intentToMain = new Intent( VetPage.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickedNewButton(View view) {
        Intent intentToNewRecord = new Intent(VetPage.this, NewPetRecording.class);
        intentToNewRecord.putExtra("email",email);
        startActivity(intentToNewRecord);
        //finish();

    }

    public void clickedSearchButton(View view) {
        enterid = binding.enterIDeditTextVetPage.getText().toString();
        firebaseFirestore.collection("PetRecordings").whereEqualTo("PetID", enterid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(VetPage.this,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if (!value.isEmpty()) {
                    Intent intent = new Intent(VetPage.this, PetPage.class);
                    intent.putExtra("keyID",enterid);
                    intent.putExtra("permission",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    i = 1;

                }else{
                    if(i == 0){
                        Toast.makeText(VetPage.this, "recording does not found for this ID", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void getData(){

        firebaseFirestore.collection("VetUsers").document(email).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String comment = (String) documentSnapshot.getData().get("Comment");
                        commentArrayList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(VetPage.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
