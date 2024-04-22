package com.berkaygunay.finalproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkaygunay.finalproject.databinding.RecyclerVetCommentsBinding;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<String> stringArrayList;

    public CommentAdapter(ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerVetCommentsBinding recyclerVetCommentsBinding = RecyclerVetCommentsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(recyclerVetCommentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.recyclerVetCommentsBinding.recyclerViewCommentTextView.setText(" - " + stringArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        RecyclerVetCommentsBinding recyclerVetCommentsBinding;

        public CommentHolder(RecyclerVetCommentsBinding recyclerVetCommentsBinding) {
            super(recyclerVetCommentsBinding.getRoot());
            this.recyclerVetCommentsBinding = recyclerVetCommentsBinding;

        }
    }
}
