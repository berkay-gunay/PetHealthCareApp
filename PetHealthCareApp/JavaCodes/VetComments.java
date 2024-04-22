package com.berkaygunay.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.berkaygunay.finalproject.databinding.ActivityVetCommentsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VetComments extends AppCompatActivity {

    private ActivityVetCommentsBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> commentArrayList;
    CommentAdapter commentAdapter;
    private String emailOfVet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVetCommentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        emailOfVet = intent.getStringExtra("vetEmail");
        firebaseFirestore = FirebaseFirestore.getInstance();
        commentArrayList = new ArrayList<>();

        getData();

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(VetComments.this));
        commentAdapter = new CommentAdapter(commentArrayList);
        binding.recyclerView2.setAdapter(commentAdapter);


    }
    public void getData(){

        firebaseFirestore.collection("VetUsers").document(emailOfVet).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String comment = (String) documentSnapshot.getData().get("Comment");
                        commentArrayList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(VetComments.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}