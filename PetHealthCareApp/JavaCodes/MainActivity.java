package com.berkaygunay.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    public String userEmail;
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            String enterEmail = user.getEmail();
            checkUser(enterEmail);
        }

    }

    public void goToVetLogin(View view){
        Intent intent = new Intent(MainActivity.this,VetLogin.class);
        startActivity(intent);
        finish();
    }
    public void goToPetOwnerLogin(View view){
        Intent intent = new Intent(MainActivity.this,PetOwnerLogin.class);
        startActivity(intent);
        finish();
    }
    public void checkUser(String enterEmail){


        firebaseFirestore.collection("PetOwnerUsers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                //getdocuments() dokumanların listesini çıkartıyor.O listenin içinde dolaşıyoruz ve Map ile değerleri çekeceğiz
                if(value != null){
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){

                        Map<String, Object> data = documentSnapshot.getData();
                        userEmail = (String) data.get("useremail");   //Casting

                        if(enterEmail.equals(userEmail)){
                            count = 1;
                        }
                    }
                    if(count == 1){
                        Intent intent = new Intent(MainActivity.this,PetOwnerPage.class);
                        intent.putExtra("email",enterEmail);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent1 = new Intent(MainActivity.this,VetPage.class);
                        intent1.putExtra("email",enterEmail);
                        startActivity(intent1);
                        finish();
                    }
                }

            }
        });
    }
}