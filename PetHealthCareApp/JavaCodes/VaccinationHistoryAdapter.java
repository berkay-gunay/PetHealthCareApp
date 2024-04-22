package com.berkaygunay.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaygunay.finalproject.databinding.RecyclerVaccHistBinding;

import java.util.ArrayList;

public class VaccinationHistoryAdapter extends RecyclerView.Adapter<VaccinationHistoryAdapter.VaccinationHistoryHolder> {
    private ArrayList<VaccineInfo> stringArrayList;

    public VaccinationHistoryAdapter(ArrayList<VaccineInfo> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public VaccinationHistoryAdapter.VaccinationHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerVaccHistBinding recyclerVaccHistBinding = RecyclerVaccHistBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new VaccinationHistoryHolder(recyclerVaccHistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccinationHistoryAdapter.VaccinationHistoryHolder holder, int position) {

        holder.recyclerVaccHistBinding.vaccineDateTextView.setText(stringArrayList.get(position).Date);
        holder.recyclerVaccHistBinding.vaccineHistInfoTextView.setText(stringArrayList.get(position).VaccineInfo);

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class VaccinationHistoryHolder extends RecyclerView.ViewHolder {
        RecyclerVaccHistBinding recyclerVaccHistBinding;
        public VaccinationHistoryHolder(RecyclerVaccHistBinding recyclerVaccHistBinding) {
            super(recyclerVaccHistBinding.getRoot());
            this.recyclerVaccHistBinding = recyclerVaccHistBinding;
        }
    }
}
