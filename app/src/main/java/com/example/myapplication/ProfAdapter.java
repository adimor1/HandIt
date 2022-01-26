package com.example.myapplication;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
    private LocationUtil locationUtil;

    public ProfAdapter(Context context, ArrayList<User> list, LocationUtil locationUtil, ProfListener profListener) {
        this.context = context;
        this.list = list;
        this.mprofListener = profListener;
        this.locationUtil = locationUtil;
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

        holder.type.setText(user.getProfession());
        holder.name.setText(user.getFirstName()+" " +user.getLastName());
        holder.email.setText(user.getEmail());
        String disStr = String.format("%.2f", user.getDistance());
        holder.dis.setText(disStr + " Km");
        Uri uri = Uri.parse(user.getUriImage());
        Picasso.get().load(uri).into(holder.imageProf);
        if(user.getCountRating()!=0){
            holder.rBar.setRating((float) (finalRatingCal(user.getSumRating() / user.getCountRating()))-1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, type, dis, email;
        ImageView imageProf;
        ProfListener profListener;
        RatingBar rBar;

        public MyViewHolder(@NonNull View itemView,  ProfListener profListener) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            dis = itemView.findViewById(R.id.tvRating);
            type = itemView.findViewById(R.id.tvType);
            imageProf = itemView.findViewById(R.id.imageProf);
            email = itemView.findViewById(R.id.tvEmail);
            rBar =  itemView.findViewById(R.id.MyRating);

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

    private double finalRatingCal(double finalRating){
        return Math.ceil(finalRating * 2) / 2;

    }
}