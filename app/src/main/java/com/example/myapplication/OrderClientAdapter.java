package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;

import java.util.ArrayList;

public class OrderClientAdapter extends RecyclerView.Adapter<OrderClientAdapter.MyViewHolder>{
    Context context;
    ArrayList<Order> list;
    ArrayList<User> listProf;
    Order order;
    User user;
    private OrderClientListener morderClientListener;

    public OrderClientAdapter(Context context, ArrayList<Order> list, ArrayList<User> listProf, OrderClientListener orderClientListener) {
        this.context = context;
        this.list = list;
        this.listProf = listProf;
        this.morderClientListener = orderClientListener;
    }

    @NonNull
    @Override
    public OrderClientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_client_item,parent,false);
        return  new MyViewHolder(v, morderClientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderClientAdapter.MyViewHolder holder, int position) {

        order = list.get(position);
        user = listProf.get(position);

        holder.time.setText(order.getDate()+" " +order.getTime());

        if(order.getStatus()==0){
            holder.status.setText("Waiting");
            holder.image.setImageResource(R.drawable.ic_wait);
        }

        if(order.getStatus()==1){
            holder.status.setText("Approv");
            holder.image.setImageResource(R.drawable.ic_approv);
        }

        if(order.getStatus()==2){
            holder.status.setText("Canceled");
            holder.image.setImageResource(R.drawable.ic_cancle);
        }

        holder.name.setText(user.getFirstName()+" "+ user.getLastName()+" - "+ user.getProfession());
        holder.phone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView time;
        TextView status;
        ImageView image;
        TextView name;
        TextView phone;

        OrderClientListener orderClientListener;

        public MyViewHolder(@NonNull View itemView,  OrderClientListener orderClientListener) {
            super(itemView);

            time = itemView.findViewById(R.id.tvTimeOrder);
            status = itemView.findViewById(R.id.statusOrder);
            image = itemView.findViewById(R.id.imageStatus);

            this.orderClientListener = orderClientListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderClientListener.orderClientClick(getAdapterPosition());
        }
    }

    public interface OrderClientListener{
        void orderClientClick(int position);
    }
}
