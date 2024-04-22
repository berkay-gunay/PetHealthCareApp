package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.berkaygunay.finalproject.databinding.ActivityPetNewInspectionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PetNewInspection extends AppCompatActivity {
    ActivityPetNewInspectionBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String petID;
    public Intent petIdIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetNewInspectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        petIdIntent = getIntent();
        petID = petIdIntent.getStringExtra("petID");

    }
    public void clickedNewInspectionSaveButton(View view){
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(cal.getTime());
        String detailInspection = binding.enterNewInspectionInfo.getText().toString();
        firebaseFirestore.collection("PetRecordings").document(petID).collection("InspectionHistory").document(currentDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String oldData;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        oldData = (String) documentSnapshot.getData().get("Inspection");
                        String newData = oldData  + "\n" + detailInspection;
                        firebaseFirestore.collection("PetRecordings").document(petID).collection("InspectionHistory").document(currentDate).update("Inspection",newData);
                    }else{
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("Inspection",binding.enterNewInspectionInfo.getText().toString());
                        data.put("Date",cal.getTime());
                        firebaseFirestore.collection("PetRecordings").document(petID).collection("InspectionHistory").document(currentDate).set(data);
                    }
                    finish();
                }else{
                    Toast.makeText(PetNewInspection.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}