package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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


        StorageReference storageReference;
        private ImageView imageProf;



        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            email = itemView.findViewById(R.id.tvemail);
            description = itemView.findViewById(R.id.tvdescription);
            profession = itemView.findViewById(R.id.tvprofession);
            imageProf = itemView.findViewById(R.id.imageProf);
            this.onNoteListener = onNoteListener;


            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference profileRef = storageReference.child("users/adimor@gmail.com/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageProf);
                }
            });

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