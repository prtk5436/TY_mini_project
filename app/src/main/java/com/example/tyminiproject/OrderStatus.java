package com.example.tyminiproject;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OrderStatus extends AppCompatActivity {

    private static final String TAG = "OrderStatus";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference request_table;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    String custMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        database = FirebaseDatabase.getInstance();
        request_table = database.getReference("Requests");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        custMobNo = Common.currentUser.getPhone();
        loadOrders(custMobNo);
    }

    private void loadOrders(String custMobNo) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item,
                OrderViewHolder.class, request_table.orderByChild("phone").equalTo(custMobNo)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request model, int i) {
                final Request local = model;
                String strOrderId = adapter.getRef(i).getKey();
                String strOrderItemPrice = local.getTotal();
                String strOrderMobNo = local.getPhone();
                String strOrderItemName = local.getName();
                Log.e(TAG, "inside loadFoodList : strOrderId : " + strOrderId);
                Log.e(TAG, "inside loadFoodList: strOrderItemPrice : " + strOrderItemPrice);
                Log.e(TAG, "inside loadFoodList : strOrderMobNo : " + strOrderMobNo);
                Log.e(TAG, "inside loadFoodList : strOrderItemName : " + strOrderItemName);
                orderViewHolder.OrderId.setText(strOrderId);
                orderViewHolder.OrderItemName.setText(strOrderItemName);
                orderViewHolder.OrderItemPrice.setText(strOrderItemPrice);
                orderViewHolder.OrderMobNo.setText(strOrderMobNo);

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}