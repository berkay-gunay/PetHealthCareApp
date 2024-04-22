package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVetLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class VetLogin extends AppCompatActivity {

    private ActivityVetLoginBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private int count = 0;
    private  int approvalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


    }

    public void goToVetSignUp(View view){
        Intent intent = new Intent(VetLogin.this,VetSignUp.class);
        startActivity(intent);
        //finish();
    }
    public void clickedVetLogin(View view){
        String enterEmail = binding.enterLoginEmailOfVet.getText().toString();
        String enterPassword = binding.enterLoginPassowrdOfVet.getText().toString();

        if(enterEmail.equals("") || enterPassword.equals("")){
            Toast.makeText(VetLogin.this, "Make sure you write everything completely.", Toast.LENGTH_SHORT).show();
        }else{
            if(!enterEmail.equals("admin@gmail.com")){
                firebaseFirestore.collection("VetUsers").document(enterEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                String approval = (String) documentSnapshot.getData().get("approval");
                                if(approval.equals("0")){
                                    Toast.makeText(VetLogin.this, "Your registration is being reviewed", Toast.LENGTH_SHORT).show();
                                }
                                if(approval.equals("1")){
                                    auth.signInWithEmailAndPassword(enterEmail,enterPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(VetLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(VetLogin.this,VetPage.class);
                                            intent.putExtra("email",enterEmail);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(VetLogin.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //approvalCount = 1;
                                }
                                if(approval.equals("2")){
                                    Toast.makeText(VetLogin.this, "This record is not approval", Toast.LENGTH_SHORT).show();
                                    //approvalCount = 2;
                                }

                            }else{
                                Toast.makeText(VetLogin.this, "Vet User for this email don't found.", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(VetLogin.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }else{//admin@gmail.com giriş yapmışsa
                auth.signInWithEmailAndPassword(enterEmail,enterPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(VetLogin.this,AdminPage.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VetLogin.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        /*if(!enterEmail.equals("admin@gmail.com")){
            firebaseFirestore.collection("VetUsers").document(enterEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            count = 1;
                            String approval = (String) documentSnapshot.getData().get("approval");
                            if(approval.equals("1")){
                                approvalCount = 1;
                            }
                            if(approval.equals("2")){
                                approvalCount = 2;
                            }
                        }else{
                            count = 0;
                        }

                    }else{
                        Toast.makeText(VetLogin.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }*/

        /*if(enterEmail.equals("") || enterPassword.equals("")){
            Toast.makeText(VetLogin.this, "Make sure you write everything completely.", Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(enterEmail,enterPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(enterEmail.equals("admin@gmail.com")){
                        Intent intent = new Intent(VetLogin.this,AdminPage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        if(count == 0){
                            Toast.makeText(VetLogin.this, "Vet User for this email don't found.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(approvalCount == 0){
                                Toast.makeText(VetLogin.this, "Your registration is being reviewed", Toast.LENGTH_SHORT).show();
                            }
                            if(approvalCount == 1){
                                Toast.makeText(VetLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VetLogin.this,VetPage.class);
                                intent.putExtra("email",enterEmail);
                                startActivity(intent);
                                finish();
                            }
                            if(approvalCount == 2){
                                Toast.makeText(VetLogin.this, "This record is not approval", Toast.LENGTH_SHORT).show();
                            }
                        }

                        
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VetLogin.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }*/

    }
}