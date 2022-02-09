package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProfAdapter extends RecyclerView.Adapter<OrderProfAdapter.MyViewHolder>{
    Context context;
    ArrayList<Order> list;
    Order order;
    private OrderProfListener morderProfListener;

    public OrderProfAdapter(Context context, ArrayList<Order> list, OrderProfListener orderProfListener) {
        this.context = context;
        this.list = list;
        this.morderProfListener = orderProfListener;
    }

    @NonNull
    @Override
    public OrderProfAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_prof_item,parent,false);
        return  new MyViewHolder(v, morderProfListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProfAdapter.MyViewHolder holder, int position) {

        order = list.get(position);
        holder.name.setText(order.getDate()+" " +order.getTime());

        if(order.getStatus()==1){
            holder.status.setText("Approv");
            holder.status.setTextColor(Color.rgb(0,128,0));
        }

        if(order.getStatus()==2){
            holder.status.setText("Canceled");
            holder.status.setTextColor(Color.rgb(178,34,34));
        }

        String place = order.getAddress();
        if(place.contains(", Israel")){
            place = place.replace(", Israel", "");
        }
        holder.phone.setText(order.getPhone());
        holder.address.setText(place);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView status;
        TextView phone;
        TextView address;
        OrderProfListener orderProfListener;

        public MyViewHolder(@NonNull View itemView,  OrderProfListener orderProfListener) {
            super(itemView);

            name = itemView.findViewById(R.id.tvTime);
            status = itemView.findViewById(R.id.status);
            phone = itemView.findViewById(R.id.tvContact);
            address = itemView.findViewById(R.id.tvPlace);

            this.orderProfListener = orderProfListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderProfListener.orderProfClick(getAdapterPosition());
        }
    }

    public interface OrderProfListener{
        void orderProfClick(int position);
    }
}
