package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityNextVaccineBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NextVaccine extends AppCompatActivity {
    ActivityNextVaccineBinding binding;
    private FirebaseFirestore firebaseFirestore;

    public String petID,kindOfPet;
    private String [] dog = new String[]{"Select","Rabies Vaccine","Distemper Vaccine","Hepatitis/Adeovirus Vaccine","Parvovirus Vaccine","Parainfluenza Vaccine","Bordetella Vaccine","Leptospirosis Vaccine","Lyme Disease Vaccine","Coronavirus Vaccine","Giardia Vaccine","Rattlesnake Vaccine","Canine Influenza H3N8 Vaccine"};
    private  String [] cat = new String[]{"Select","Bordetella Vaccine","Feline Leukemia Virus (FeLV) Vaccine","The FVRCP Vaccine","Rabies Vaccine"};
    private ArrayAdapter<String> arrayAdapter;
    private int w;
    private String vaccine = "",day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextVaccineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        petID = intent.getStringExtra("petid");

        getdata();
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
                            arrayAdapter = new ArrayAdapter<>(NextVaccine.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dog);
                            w=0;
                        }
                        if(kindOfPet.equals("Cat")){
                            arrayAdapter = new ArrayAdapter<>(NextVaccine.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,cat);
                            w=1;
                        }
                        binding.nextVaccineSpinner.setAdapter(arrayAdapter);
                        int initialposition = binding.nextVaccineSpinner.getSelectedItemPosition();
                        binding.nextVaccineSpinner.setSelection(initialposition,false);
                        binding.nextVaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        Toast.makeText(NextVaccine.this, "no such document", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(NextVaccine.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickedSaveButtonNextVaccine(View view){

        if(vaccine.equals("")){
            Toast.makeText(this, "Please select vaccine", Toast.LENGTH_SHORT).show();
        }else{
            day = String.valueOf(binding.nextVaccineDatePicker.getDayOfMonth());
            month = String.valueOf(binding.nextVaccineDatePicker.getMonth());
            year = String.valueOf(binding.nextVaccineDatePicker.getYear());
            int hour = binding.timePicker2.getHour();
            int minute = binding.timePicker2.getMinute();
            Calendar cal = Calendar.getInstance();
            cal.set(binding.nextVaccineDatePicker.getYear(),binding.nextVaccineDatePicker.getMonth(),binding.nextVaccineDatePicker.getDayOfMonth(),hour,minute,0);
            String nextDate = DateFormat.getDateInstance().format(cal.getTime());
            Map<String,String> dateInfo = new HashMap<>();
            dateInfo.put("day",day);
            dateInfo.put("month",month);
            dateInfo.put("year",year);
            dateInfo.put("hour",String.valueOf(hour));
            dateInfo.put("minute",String.valueOf(minute));
            dateInfo.put("Vaccine",vaccine);
            dateInfo.put("count","0");
            firebaseFirestore.collection("PetRecordings").document(petID).collection("NextVaccines").document(nextDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String oldData;
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            oldData = (String) documentSnapshot.getData().get("Vaccine");
                            String newData = oldData + "\n" + vaccine;
                            firebaseFirestore.collection("PetRecordings").document(petID).collection("NextVaccines").document(nextDate).update("Vaccine",newData);
                        }else{

                            firebaseFirestore.collection("PetRecordings").document(petID).collection("NextVaccines").document(nextDate).set(dateInfo);
                        }
                        finish();
                    }else{
                        Toast.makeText(NextVaccine.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }
}