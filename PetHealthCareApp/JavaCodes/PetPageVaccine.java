package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityPetPageVaccineBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PetPageVaccine extends AppCompatActivity {
    private ActivityPetPageVaccineBinding binding;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<VaccineInfo> vaccineInfoArrayList;
    ArrayList<NextVaccineInfo> NextVaccineInfoArrayList;
    private String petId,date,vaccine;
    private int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetPageVaccineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        createNotificationChannel();
        vaccineInfoArrayList = new ArrayList<>();
        NextVaccineInfoArrayList = new ArrayList<>();
        Intent intent = getIntent();
        petId = intent.getStringExtra("petID");
        permission = intent.getIntExtra("permission",0);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("PetRecordings").document(petId).collection("VaccinationHistory").orderBy("Date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(PetPageVaccine.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!value.isEmpty()){
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                        date = documentSnapshot.getId();
                        vaccine = (String) documentSnapshot.getData().get("Vaccine");
                        VaccineInfo vaccineInfo = new VaccineInfo(date,vaccine);
                        vaccineInfoArrayList.add(vaccineInfo);

                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(PetPageVaccine.this, android.R.layout.simple_list_item_1,
                            vaccineInfoArrayList.stream().map(VaccineInfo -> VaccineInfo.Date).collect(Collectors.toList())
                    );
                    binding.listView.setAdapter(arrayAdapter);
                    binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PetPageVaccine.this);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage(vaccineInfoArrayList.get(i).VaccineInfo);
                            alertDialog.setNeutralButton("OK",null);
                            alertDialog.show();
                        }
                    });
                }
            }
        });

    getNextVaccines();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vaccine_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = getIntent();
        petId = intent.getStringExtra("petID");
        if(item.getItemId() == R.id.new_vaccine){
            if(permission == 1){
                vaccineInfoArrayList.clear();
                Intent intent4 = new Intent(PetPageVaccine.this, NewVaccine.class);
                intent4.putExtra("petid",petId);
                startActivity(intent4);
                //finish();
            }else{
                Toast.makeText(this, "You don't have a permission.", Toast.LENGTH_SHORT).show();
            }

        }
        if(item.getItemId() == R.id.next_vaccine){
            if(permission == 1){
                Intent intent2 = new Intent(PetPageVaccine.this,NextVaccine.class);
                intent2.putExtra("petid",petId);
                startActivity(intent2);
            }else{
                Toast.makeText(this, "You don't have a permission.", Toast.LENGTH_SHORT).show();

            }
        }
        if(item.getItemId() == R.id.vaccinationHistoryPage){
            Intent goToVaccinationHistoryPage = new Intent(PetPageVaccine.this,VaccinationHistoryPage.class);
            goToVaccinationHistoryPage.putExtra("petid",petId);
            startActivity(goToVaccinationHistoryPage);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getNextVaccines(){

        firebaseFirestore.collection("PetRecordings").document(petId).collection("NextVaccines").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(PetPageVaccine.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(!value.isEmpty()){
                    NextVaccineInfoArrayList.clear();
                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                        String nextVaccineDate = documentSnapshot.getId();
                        String nextVaccine = (String) documentSnapshot.getData().get("Vaccine");
                        String year = (String) documentSnapshot.getData().get("year");
                        String month = (String) documentSnapshot.getData().get("month");
                        String day = (String) documentSnapshot.getData().get("day");
                        String hour = (String) documentSnapshot.getData().get("hour");
                        String minute = (String) documentSnapshot.getData().get("minute");
                        String count = (String) documentSnapshot.getData().get("count");
                        NextVaccineInfo nextVaccineInfo = new NextVaccineInfo(nextVaccineDate,day,month,year,hour,minute,nextVaccine,count);
                        NextVaccineInfoArrayList.add(nextVaccineInfo);

                    }
                    ArrayAdapter arrayAdapter1 = new ArrayAdapter(PetPageVaccine.this, android.R.layout.simple_list_item_1,
                            NextVaccineInfoArrayList.stream().map(NextVaccineInfo -> NextVaccineInfo.date).collect(Collectors.toList())
                    );
                    binding.nextVaccinesListView.setAdapter(arrayAdapter1);
                    binding.nextVaccinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String checkCount = NextVaccineInfoArrayList.get(i).count;
                            if(permission == 0){
                                if(checkCount.equals("0")){
                                    Intent intent = new Intent(PetPageVaccine.this,ReminderBroadcast.class);
                                    //intent.putExtra("Vaccine",NextVaccineInfoArrayList.get(i).vaccine);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(PetPageVaccine.this,1,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    Calendar cal = Calendar.getInstance();
                                    int year = Integer.parseInt(NextVaccineInfoArrayList.get(i).year);
                                    int month = Integer.parseInt(NextVaccineInfoArrayList.get(i).month);
                                    int day = Integer.parseInt(NextVaccineInfoArrayList.get(i).day);
                                    int hour = Integer.parseInt(NextVaccineInfoArrayList.get(i).hour);
                                    int minute = Integer.parseInt(NextVaccineInfoArrayList.get(i).minute);
                                    cal.set(year,month,day,hour,minute);
                                    long time = cal.getTimeInMillis();
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                            time,
                                            pendingIntent);

                                    firebaseFirestore.collection("PetRecordings").document(petId).collection("NextVaccines").document(NextVaccineInfoArrayList.get(i).date).update("count","1");
                                    NextVaccineInfoArrayList.clear();

                                    Toast.makeText(PetPageVaccine.this, "Reminder created", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PetPageVaccine.this,"already set reminder for this day", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(PetPageVaccine.this, "You don't have a permission.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                    binding.nextVaccinesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(permission == 0){
                                AlertDialog.Builder alert = new AlertDialog.Builder(PetPageVaccine.this);
                                alert.setTitle("Delete This Record");
                                alert.setMessage("Are you sure that you want to delete record");
                                alert.setNegativeButton("No",null);
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int p) {
                                        firebaseFirestore.collection("PetRecordings").document(petId).collection("NextVaccines").document(NextVaccineInfoArrayList.get(i).date).delete();
                                        NextVaccineInfoArrayList.clear();
                                        arrayAdapter1.clear();

                                    }
                                });
                                alert.show();
                            }else{
                                Toast.makeText(PetPageVaccine.this, "You don't have a permission.", Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        }
                    });
                }

            }
        });
    }
    private void createNotificationChannel(){
        CharSequence name = "VaccineReminderChannel";
        String description = "Channel for Vaccine Reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("channel1",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}