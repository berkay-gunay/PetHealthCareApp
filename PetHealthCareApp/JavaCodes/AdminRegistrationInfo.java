package com.berkaygunay.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.berkaygunay.finalproject.databinding.ActivityAdminRegistrationInfoBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminRegistrationInfo extends AppCompatActivity {
    ActivityAdminRegistrationInfoBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private RegistrationInfo registrationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminRegistrationInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        registrationInfo = (RegistrationInfo) intent.getSerializableExtra("registrationInfo");
        binding.nameSurnameTextView.setText(registrationInfo.nameSurname);
        binding.clinicNameTextView.setText(registrationInfo.clinicName);
        binding.diplomaNumberTextView.setText(registrationInfo.diplomaNumber);
        binding.emailTextView.setText(registrationInfo.email);
        binding.phoneNumberTextView.setText(registrationInfo.phoneNumber);
        binding.cityTextView.setText(registrationInfo.city);
        binding.districtTextView.setText(registrationInfo.district);
        binding.addressTextView.setText(registrationInfo.address);
    }
    public void clickedApproveButton(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminRegistrationInfo.this);
        alert.setTitle("?");
        alert.setMessage("Are you sure?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseFirestore.collection("VetUsers").document(registrationInfo.email).update("approval","1");
                //Intent intent = new Intent(AdminRegistrationInfo.this,AdminPage.class);
                //startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }
    public void clickedRejectButton(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminRegistrationInfo.this);
        alert.setTitle("?");
        alert.setMessage("Are you sure?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseFirestore.collection("VetUsers").document(registrationInfo.email).update("approval","2");
                //Intent intent = new Intent(AdminRegistrationInfo.this,AdminPage.class);
                //startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }
}