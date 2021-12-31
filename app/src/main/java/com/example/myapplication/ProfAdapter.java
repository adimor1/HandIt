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

import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;
    private ProfListener mprofListener;

    public ProfAdapter(Context context, ArrayList<User> list, ProfListener profListener) {
        this.context = context;
        this.list = list;
        this.mprofListener = profListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.prof_item,parent,false);
        return  new MyViewHolder(v, mprofListener);
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
        ProfListener profListener;

        StorageReference storageReference;
        private ImageView imageProf;

        public MyViewHolder(@NonNull View itemView, ProfListener profListener) {
            super(itemView);

            email = itemView.findViewById(R.id.tvemail);
            description = itemView.findViewById(R.id.tvdescription);
            profession = itemView.findViewById(R.id.tvprofession);
            imageProf = itemView.findViewById(R.id.imageProf);
            this.profListener = profListener;


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
            profListener.profClick(getAdapterPosition());
        }
    }

    public interface ProfListener{
        void profClick(int position);
    }
}