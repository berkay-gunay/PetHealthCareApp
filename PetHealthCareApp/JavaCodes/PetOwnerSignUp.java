package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityPetOwnerSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class PetOwnerSignUp extends AppCompatActivity {

    private ActivityPetOwnerSignUpBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    String email;
    String password;
    String namesurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetOwnerSignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void clickedPetOwnerComplete(View view){

        email = binding.enterPetOwnerEmail.getText().toString();
        password = binding.enterPetOwnerPassword.getText().toString();
        namesurname = binding.EnterNameSurname.getText().toString();

        if(email.equals("") || password.equals("") || namesurname.equals("")){
            Toast.makeText(this, "Make sure you write everything completely", Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //auth.getUid().toString();     burada firestore a kaydedeceğiz
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("useremail", email);
                    //userData.put("useruid", auth.getUid());
                    firebaseFirestore.collection("PetOwnerUsers").document(email).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PetOwnerSignUp.this, "Completed!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PetOwnerSignUp.this,PetOwnerLogin.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PetOwnerSignUp.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });




                /*addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(PetOwnerSignUp.this, "Completed!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PetOwnerSignUp.this,PetOwnerLogin.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PetOwnerSignUp.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    /*Toast.makeText(PetOwnerSignUp.this, "Completed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PetOwnerSignUp.this,PetOwnerLogin.class);
                    startActivity(intent);
                    finish();*/
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PetOwnerSignUp.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}