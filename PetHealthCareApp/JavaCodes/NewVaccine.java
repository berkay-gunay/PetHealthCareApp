package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityNewVaccineBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewVaccine extends AppCompatActivity {

    ActivityNewVaccineBinding binding;
    private FirebaseFirestore firebaseFirestore;

    public String petID,kindOfPet;
    private String [] dog = new String[]{"Select","Rabies Vaccine","Distemper Vaccine","Hepatitis/Adeovirus Vaccine","Parvovirus Vaccine","Parainfluenza Vaccine","Bordetella Vaccine","Leptospirosis Vaccine","Lyme Disease Vaccine","Coronavirus Vaccine","Giardia Vaccine","Rattlesnake Vaccine","Canine Influenza H3N8 Vaccine"};
    private  String [] cat = new String[]{"Select","Bordetella Vaccine","Feline Leukemia Virus (FeLV) Vaccine","The FVRCP Vaccine","Rabies Vaccine"};
    private ArrayAdapter<String> arrayAdapter;
    private int w;
    private String vaccine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewVaccineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        petID = intent.getStringExtra("petid");

        getdata();
        //dog ve cat array lerini doldur akabinde yazılanları firebase de Pets in içinde doc adı tarih olacak şekilde kaydet. Daha sonra bunları listview ile sıralayacağız

    }
    public void getdata(){
        firebaseFirestore.collection("PetRecordings").document(petID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        kindOfPet = (String) documentSnapshot.getData().get("KindOfThisPet");
                        //binding.infoTextView.setText(kindOfPet);
                        if(kindOfPet.equals("Dog")){
                            arrayAdapter = new ArrayAdapter<>(NewVaccine.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dog);
                            w=0;
                        }
                        if(kindOfPet.equals("Cat")){
                            arrayAdapter = new ArrayAdapter<>(NewVaccine.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,cat);
                            w=1;
                        }
                        binding.whichVaccineSpinner.setAdapter(arrayAdapter);
                        int initialposition = binding.whichVaccineSpinner.getSelectedItemPosition();
                        binding.whichVaccineSpinner.setSelection(initialposition,false);
                        binding.whichVaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i != 0){
                                    if(w == 0){
                                        vaccine = dog[i];
                                    }
                                    if(w == 1){
                                        vaccine = cat[i];
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                vaccine = "";
                            }
                        });
                    }else{
                        Toast.makeText(NewVaccine.this, "no such document", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(NewVaccine.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void clickedSaveButton(View view){
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(cal.getTime());
        String detailVaccine = binding.enterDetailVaccine.getText().toString();
        firebaseFirestore.collection("PetRecordings").document(petID).collection("VaccinationHistory").document(currentDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String oldData;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        oldData = (String) documentSnapshot.getData().get("Vaccine");
                        String newData = oldData + "\n"+ "\n" + vaccine + "\n" + detailVaccine;
                        firebaseFirestore.collection("PetRecordings").document(petID).collection("VaccinationHistory").document(currentDate).update("Vaccine",newData);
                    }else{
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("Vaccine",vaccine + "\n" + binding.enterDetailVaccine.getText().toString());
                        data.put("Date",cal.getTime());
                        firebaseFirestore.collection("PetRecordings").document(petID).collection("VaccinationHistory").document(currentDate).set(data);
                    }
                    finish();
                }else{
                    Toast.makeText(NewVaccine.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}