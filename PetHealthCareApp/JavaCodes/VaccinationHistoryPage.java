package com.berkaygunay.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVaccinationHistoryPageBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VaccinationHistoryPage extends AppCompatActivity {
    ActivityVaccinationHistoryPageBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String petid,date,vaccine;
    private ArrayList<VaccineInfo> arrayList;
    VaccinationHistoryAdapter vaccinationHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVaccinationHistoryPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        getdata();

        binding.vaccHistPageRecyclerView.setLayoutManager(new LinearLayoutManager(VaccinationHistoryPage.this));
        vaccinationHistoryAdapter = new VaccinationHistoryAdapter(arrayList);
        binding.vaccHistPageRecyclerView.setAdapter(vaccinationHistoryAdapter);

    }

    public void getdata(){
        Intent getPetID = getIntent();
        petid = getPetID.getStringExtra("petid");

        firebaseFirestore.collection("PetRecordings").document(petid).collection("VaccinationHistory").orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(VaccinationHistoryPage.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!value.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        date = documentSnapshot.getId();
                        vaccine = (String) documentSnapshot.getData().get("Vaccine");
                        VaccineInfo vaccineInfo = new VaccineInfo(date, vaccine);
                        arrayList.add(vaccineInfo);
                    }
                }
                vaccinationHistoryAdapter.notifyDataSetChanged();
            }
        });
    }
}