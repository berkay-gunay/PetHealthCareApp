package com.berkaygunay.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaygunay.finalproject.databinding.RecyclerInspectionHistoryBinding;

import java.util.ArrayList;

public class InspectionHistoryAdapter extends RecyclerView.Adapter<InspectionHistoryAdapter.InspectionHistoryHolder> {

    private ArrayList<InspectionInfo> inspectionInfoArrayList;

    public InspectionHistoryAdapter(ArrayList<InspectionInfo> inspectionInfoArrayList) {
        this.inspectionInfoArrayList = inspectionInfoArrayList;
    }

    @NonNull
    @Override
    public InspectionHistoryAdapter.InspectionHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerInspectionHistoryBinding recyclerInspectionHistoryBinding = RecyclerInspectionHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new InspectionHistoryHolder(recyclerInspectionHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionHistoryAdapter.InspectionHistoryHolder holder, int position) {
        holder.recyclerInspectionHistoryBinding.InspectionDateTextView.setText(inspectionInfoArrayList.get(position).date);
        holder.recyclerInspectionHistoryBinding.InspectionInfoTextView.setText(inspectionInfoArrayList.get(position).insceptionInfo);
    }

    @Override
    public int getItemCount() {
        return inspectionInfoArrayList.size();
    }

    public class InspectionHistoryHolder extends RecyclerView.ViewHolder {

        RecyclerInspectionHistoryBinding recyclerInspectionHistoryBinding;

        public InspectionHistoryHolder(RecyclerInspectionHistoryBinding recyclerInspectionHistoryBinding) {
            super(recyclerInspectionHistoryBinding.getRoot());
            this.recyclerInspectionHistoryBinding = recyclerInspectionHistoryBinding;

        }
    }
}
