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

import com.berkaygunay.finalproject.databinding.ActivityPetInspectionBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PetInspection extends AppCompatActivity {

    ActivityPetInspectionBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String petID,date,inspection;
    ArrayList<InspectionInfo> inspectionInfoArrayList;
    private InspectionHistoryAdapter inspectionHistoryAdapter;
    private int permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetInspectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        inspectionInfoArrayList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        petID = intent.getStringExtra("petid");
        permission = intent.getIntExtra("permission",0);


        getData();

        binding.PetInspectionRecyclerView.setLayoutManager(new LinearLayoutManager(PetInspection.this));
        inspectionHistoryAdapter = new InspectionHistoryAdapter(inspectionInfoArrayList);
        binding.PetInspectionRecyclerView.setAdapter(inspectionHistoryAdapter);

    }
    public void getData(){


        firebaseFirestore.collection("PetRecordings").document(petID).collection("InspectionHistory").orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(PetInspection.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!value.isEmpty()) {
                    inspectionInfoArrayList.clear();
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        date = documentSnapshot.getId();
                        inspection = (String) documentSnapshot.getData().get("Inspection");
                        InspectionInfo inspectionInfo = new InspectionInfo(date,inspection);
                        inspectionInfoArrayList.add(inspectionInfo);
                    }
                }
                inspectionHistoryAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pet_inspection_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.NewInspectionItem){
            if(permission == 1){
                Intent goToNewinspection = new Intent(PetInspection.this, PetNewInspection.class);
                goToNewinspection.putExtra("petID",petID);
                startActivity(goToNewinspection);

            }else{
                Toast.makeText(this, "You don't have a permission.", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}