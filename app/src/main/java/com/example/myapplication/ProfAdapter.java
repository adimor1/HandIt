package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;
    User user;
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
        user = list.get(position);
        holder.rating.setText(user.getEmail());
        holder.description.setText(user.getDescription());
        holder.name.setText(user.getProfession());
        holder.email.setText(user.getEmail());
        Uri uri = Uri.parse(user.getUriImage());
        Picasso.get().load(uri).into(holder.imageProf);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, description, rating, email;
        ImageView imageProf;
        ProfListener profListener;

        public MyViewHolder(@NonNull View itemView,  ProfListener profListener) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.tvRating);
            description = itemView.findViewById(R.id.tvDescription);
            imageProf = itemView.findViewById(R.id.imageProf);
            email = itemView.findViewById(R.id.tvEmail);
            this.profListener = profListener;

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