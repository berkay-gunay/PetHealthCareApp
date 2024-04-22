package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityNewPetRecordingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class NewPetRecording extends AppCompatActivity {
    ActivityNewPetRecordingBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String [] kindPet = new String[]{"Kind of Pet","Dog","Cat"};
    private ArrayAdapter<String> arrayAdapter;

    public String id,kindOfThisPet,info,petname,emailofpetowner,emailofvet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPetRecordingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        arrayAdapter = new ArrayAdapter<>(NewPetRecording.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,kindPet);
        binding.spinner.setAdapter(arrayAdapter);
        int initialPosition = binding.spinner.getSelectedItemPosition();
        binding.spinner.setSelection(initialPosition,false);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 0){
                    kindOfThisPet = kindPet[position];

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kindOfThisPet = "";
            }
        });



    }
    public void clickedCreateRecordButton(View view){
        petname = binding.enterPetName.getText().toString();
        id = binding.enterPetID.getText().toString();
        info = binding.enterInformation.getText().toString();
        emailofpetowner = binding.emailOfPetOwner.getText().toString();
        Intent intent1 = getIntent();
        emailofvet = intent1.getStringExtra("email");
        if(id.equals("") || kindOfThisPet.equals("") || info.equals("") || petname.equals("")){
            Toast.makeText(NewPetRecording.this, "Make sure you write everything completely", Toast.LENGTH_SHORT).show();
        }else{

            HashMap<String, Object> petInfo = new HashMap<>();
            petInfo.put("Information", info);
            petInfo.put("KindOfThisPet", kindOfThisPet);
            petInfo.put("PetID",id);
            petInfo.put("PetName",petname);
            petInfo.put("EmailOfPetOwner",emailofpetowner);
            petInfo.put("Date", FieldValue.serverTimestamp());
            petInfo.put("EmailOfVet",emailofvet);

            DocumentReference docref = firebaseFirestore.collection("PetOwnerUsers").document(emailofpetowner);
            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            firebaseFirestore.collection("PetRecordings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        if(documentSnapshot1.exists()){
                                            Toast.makeText(NewPetRecording.this,"there is a record for this PetID", Toast.LENGTH_SHORT).show();
                                        }else{
                                            firebaseFirestore.collection("PetRecordings").document(id).set(petInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(NewPetRecording.this, "Completed!", Toast.LENGTH_SHORT).show();
                                                    //Intent intent1 = new Intent(NewPetRecording.this,VetPage.class);
                                                    //startActivity(intent1);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(NewPetRecording.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }else{
                                        Toast.makeText(NewPetRecording.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else{
                            Toast.makeText(NewPetRecording.this, "This Pet Owner Email is not found", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(NewPetRecording.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}