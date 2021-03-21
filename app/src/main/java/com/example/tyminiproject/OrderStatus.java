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
    String custMobNo, messName, checkUserType;

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


        if (getIntent() != null) {
            messName = getIntent().getStringExtra("messName");
            loadMessOrders(messName);
            Log.d(TAG, "onCreate: messName--- " + messName);
            if (messName == null) {
                custMobNo = Common.currentUser.getPhone();
                Log.d(TAG, "onCreate: custMobNo--- " + custMobNo);
                loadOrders(custMobNo);
            }
        }
    }

    private void loadMessOrders(String messName) {
        Log.d(TAG, "inside loadMessOrders : messName---" + messName);
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item,
                OrderViewHolder.class, request_table.orderByChild("messName").equalTo(messName)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request model, int i) {
                final Request local = model;
                String strOrderId = adapter.getRef(i).getKey();
                String strOrderItemPrice = local.getTotalPrice();
                String strOrderMobNo = local.getPhone();
                String strOrderItemName = local.getMenu();
                String strOrderMessName = local.getMessName();
                String strCustName = local.getCustName();
                Log.e(TAG, "inside loadFoodList : strOrderId : " + strOrderId);
                Log.e(TAG, "inside loadFoodList: strOrderItemPrice : " + strOrderItemPrice);
                Log.e(TAG, "inside loadFoodList : strOrderMobNo : " + strOrderMobNo);
                Log.e(TAG, "inside loadFoodList : strOrderItemName : " + strOrderItemName);
                Log.e(TAG, "inside loadFoodList : strOrderMessName : " + strOrderMessName);
                Log.e(TAG, "inside loadFoodList : strCustName : " + strCustName);
                orderViewHolder.OrderId.setText(strOrderId);
                orderViewHolder.OrderItemName.setText(strOrderItemName);
                orderViewHolder.OrderItemPrice.setText(strOrderItemPrice);
                orderViewHolder.OrderMobNo.setText(strOrderMobNo);
                orderViewHolder.OrderMessName.setText(strCustName);

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }

    private void loadOrders(String custMobNo) {
        Log.d(TAG, "inside loadOrders : MobNo---" + custMobNo);
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item,
                OrderViewHolder.class, request_table.orderByChild("phone").equalTo(custMobNo)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request model, int i) {
                final Request local = model;
                String strOrderId = adapter.getRef(i).getKey();
                String strOrderItemPrice = local.getTotalPrice();
                String strOrderMobNo = local.getPhone();
                String strOrderItemName = local.getMenu();
                String strOrderMessName = local.getMessName();
                String strCustName = local.getCustName();
                Log.e(TAG, "inside loadFoodList : strOrderId : " + strOrderId);
                Log.e(TAG, "inside loadFoodList: strOrderItemPrice : " + strOrderItemPrice);
                Log.e(TAG, "inside loadFoodList : strOrderMobNo : " + strOrderMobNo);
                Log.e(TAG, "inside loadFoodList : strOrderItemName : " + strOrderItemName);
                Log.e(TAG, "inside loadFoodList : strOrderMessName : " + strOrderMessName);
                Log.e(TAG, "inside loadFoodList : strCustName : " + strCustName);
                orderViewHolder.OrderId.setText(strOrderId);
                orderViewHolder.OrderItemName.setText(strOrderItemName);
                orderViewHolder.OrderItemPrice.setText(strOrderItemPrice);
                orderViewHolder.OrderMobNo.setText(strOrderMobNo);
                orderViewHolder.OrderMessName.setText(strOrderMessName);

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}