package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<User> list;
    private OnNoteListener monNoteListener;


    public MyAdapter(Context context, ArrayList<User> list, OnNoteListener onNoteListener) {
        this.context = context;
        this.list = list;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v, monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = list.get(position);
        holder.email.setText(user.getEmail());
        holder.description.setText(user.getDescription());
        holder.profession.setText(user.getProfession());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView email, description, profession;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            email = itemView.findViewById(R.id.tvemail);
            description = itemView.findViewById(R.id.tvdescription);
            profession = itemView.findViewById(R.id.tvprofession);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNodeClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNodeClick(int position);
    }
}