package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityAdminPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AdminPage extends AppCompatActivity {
    ActivityAdminPageBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ArrayList<RegistrationInfo> arrayList;
    String nameSurname,clinicName,diplomaNumber,phoneNumber,email,city,district,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        arrayList = new ArrayList<>();
        addData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.AdminSignOutItem){
            auth.signOut();
            Intent intentToMain = new Intent( AdminPage.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addData(){

        firebaseFirestore.collection("VetUsers").whereEqualTo("approval","0").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(AdminPage.this,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!value.isEmpty()){
                    arrayList.clear();
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){

                        nameSurname = (String) documentSnapshot.getData().get("NameSurname");
                        clinicName = (String) documentSnapshot.getData().get("ClinicName");
                        diplomaNumber= (String) documentSnapshot.getData().get("DiplomaNumber");
                        phoneNumber = (String) documentSnapshot.getData().get("PhoneNumber");
                        email = (String) documentSnapshot.getData().get("UserEmail");
                        city = (String) documentSnapshot.getData().get("City");
                        district = (String) documentSnapshot.getData().get("District");
                        address = (String) documentSnapshot.getData().get("Address");
                        RegistrationInfo registrationInfo = new RegistrationInfo(nameSurname,clinicName,diplomaNumber,phoneNumber,email,city,district,address);
                        arrayList.add(registrationInfo);
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<>(AdminPage.this, android.R.layout.simple_list_item_1,
                            arrayList.stream().map(RegistrationInfo -> RegistrationInfo.nameSurname).collect(Collectors.toList()));
                    binding.listView.setAdapter(arrayAdapter);
                    binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(AdminPage.this,AdminRegistrationInfo.class);
                            intent.putExtra("registrationInfo",arrayList.get(i));
                            startActivity(intent);
                            //arrayAdapter.clear();
                        }
                    });
                }
            }
        });
    }
}