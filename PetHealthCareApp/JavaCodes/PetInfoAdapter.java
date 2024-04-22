package com.berkaygunay.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaygunay.finalproject.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class PetInfoAdapter extends RecyclerView.Adapter<PetInfoAdapter.PetInfoHolder> {

    private ArrayList<PetInfo> petInfoArrayList;

    public PetInfoAdapter(ArrayList<PetInfo> petInfoArrayList) {
        this.petInfoArrayList = petInfoArrayList;
    }

    @NonNull
    @Override
    public PetInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PetInfoHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PetInfoHolder holder, int position) {
        holder.recyclerRowBinding.petNameTextView.setText(petInfoArrayList.get(position).petname);
        holder.recyclerRowBinding.idTextView.setText(petInfoArrayList.get(position).id);
        holder.recyclerRowBinding.kindTextView.setText(petInfoArrayList.get(position).kindofthispet);
        holder.recyclerRowBinding.infoTextView.setText(petInfoArrayList.get(position).information);
        holder.recyclerRowBinding.dateTextView.setText(petInfoArrayList.get(position).date.toDate().toString());
        holder.recyclerRowBinding.emailPetOwnertextView.setText(petInfoArrayList.get(position).emailofPetOwner);

    }

    @Override
    public int getItemCount() {
        return petInfoArrayList.size();
    }

    class PetInfoHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public PetInfoHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
