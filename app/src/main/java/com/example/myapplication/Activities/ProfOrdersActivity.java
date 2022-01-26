package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.OrderProfAdapter;
import com.example.myapplication.ProfAdapter;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ProfOrdersActivity extends AppCompatActivity implements OrderProfAdapter.OrderProfListener{

    RecyclerView recyclerView;
    DatabaseReference database;
    OrderProfAdapter myAdapter;
    ArrayList<Order> list;
    ArrayList<Double> disList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_orders);
        adapterHandler();
    }

    private void adapterHandler(){
        recyclerView = findViewById(R.id.orderList);
        database = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        myAdapter = new OrderProfAdapter(this, list, this);
        new ItemTouchHelper(itemTouchHelperCallbeck).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(itemTouchHelperCallbeck2).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(myAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                        if((order.getProEmail()).equals(LoginUser.getLoginEmail())){
                            list.add(order);
                        }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    @Override
    public void orderProfClick(int position) {

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallbeck = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String id = list.get(viewHolder.getAdapterPosition()).getid();
            updateStatus(2, id);
            myAdapter.notifyDataSetChanged();
            final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.black));
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ProfOrdersActivity.this, R.color.red))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    ItemTouchHelper.SimpleCallback itemTouchHelperCallbeck2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String id = list.get(viewHolder.getAdapterPosition()).getid();
            updateStatus(1, id);
            myAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ProfOrdersActivity.this, R.color.green))
                    .addSwipeLeftActionIcon(R.drawable.ic_3)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void updateStatus(int status, String id) {
        HashMap Order = new HashMap();
        Order.put("status", status);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    databaseReference.child(key).updateChildren(Order);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}