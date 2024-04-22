package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityPetOwnerLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class PetOwnerLogin extends AppCompatActivity {

    private ActivityPetOwnerLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private String userEmail;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetOwnerLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        /*if(user != null){
            String enterEmail = user.getEmail();
            Intent intent = new Intent(PetOwnerLogin.this,PetOwnerPage.class);
            intent.putExtra("email",enterEmail);
            startActivity(intent);
            finish();
        }*/

    }

    public void goToPetOwnerSignUp(View view){
        Intent intent = new Intent(PetOwnerLogin.this,PetOwnerSignUp.class);
        startActivity(intent);
        finish();
    }
    public void clickedPetOwnerLogin(View view){

        String enterEmail = binding.enterLoginEmailOfPetOwner.getText().toString();
        String enterPassword = binding.enterLoginPasswordOfPetOwner.getText().toString();

        firebaseFirestore.collection("PetOwnerUsers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(PetOwnerLogin.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                //getdocuments() dokumanların listesini çıkartıyor.O listenin içinde dolaşıyoruz ve Map ile değerleri çekeceğiz
                if(value != null){
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){

                        Map<String, Object> data = documentSnapshot.getData();
                        userEmail = (String) data.get("useremail");   //Casting

                        if(enterEmail.equals(userEmail)){
                            count++;
                        }
                    }
                }

            }
        });

        if(enterEmail.equals("") || enterPassword.equals("")){
            Toast.makeText(this, "Make sure you write everything completely.", Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(enterEmail,enterPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(count == 0){
                        Toast.makeText(PetOwnerLogin.this, "Pet Owner User for this email don't found. ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PetOwnerLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PetOwnerLogin.this,PetOwnerPage.class);
                        intent.putExtra("email",enterEmail);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PetOwnerLogin.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}