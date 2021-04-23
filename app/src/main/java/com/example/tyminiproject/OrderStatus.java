package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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

    int flag = 0;


    ProgressBar progressBar;
    TextView tvNODATAFOUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        TextView tv_userType = findViewById(R.id.tv_userType);
        ImageView imgBackMyOrders = findViewById(R.id.imgBackMyOrders);

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
            //loadMessOrders(messName);  //load orders at mess owner side
            flag = 1;
            Log.d(TAG, "onCreate: messName--- " + messName);
            if (messName == null) {
                custMobNo = Common.currentUser.getPhone();
                Log.d(TAG, "onCreate: custMobNo--- " + custMobNo);
               // loadOrders(custMobNo);//load orders at customer side
                flag = 0;
            }
        }


        tvNODATAFOUND = findViewById(R.id.tvNOTFOUND);
        progressBar = findViewById(R.id.progressbar);

        request_table.orderByChild("messName").equalTo(messName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    tvNODATAFOUND.setVisibility(View.GONE);
                    loadMessOrders(messName);  //load orders at mess owner side
                    // Toast.makeText(FoodList.this, "data exists", Toast.LENGTH_SHORT).show();

                } else {

                    tvNODATAFOUND.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(FoodList.this, "No data exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        request_table.orderByChild("phone").equalTo(custMobNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    tvNODATAFOUND.setVisibility(View.GONE);
                    loadOrders(custMobNo);//load orders at customer side
                    // Toast.makeText(FoodList.this, "data exists", Toast.LENGTH_SHORT).show();

                } else {

                    tvNODATAFOUND.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(FoodList.this, "No data exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imgBackMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG, "onCreate: FLAG --- " + flag);

                if (flag == 0) {

                    Intent i = new Intent(OrderStatus.this, Home.class);
                    startActivity(i);
                    finish();
                }

                if (flag == 1) {

                    Intent i = new Intent(OrderStatus.this, MessOwnerHome.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }


    //only use for mess side
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
                String strFoodImg = local.getImage();
                String strQty = local.getQuantity();

                Log.d(TAG, "inside loadOrders : strOrderId : " + strOrderId);
                Log.d(TAG, "inside loadOrders: strOrderItemPrice : " + strOrderItemPrice);
                Log.d(TAG, "inside loadOrders : strOrderMobNo : " + strOrderMobNo);
                Log.d(TAG, "inside loadOrders : strOrderItemName : " + strOrderItemName);
                Log.d(TAG, "inside loadOrders : strOrderMessName : " + strOrderMessName);
                Log.d(TAG, "inside loadOrders : strCustName : " + strCustName);
                Log.d(TAG, "inside loadOrders : strFoodImg : " + strFoodImg);

                Log.d(TAG, "inside loadOrders : strQty : " + strQty);

                orderViewHolder.tv_userType.setText("Customer : ");

                orderViewHolder.OrderId.setText(strOrderId);
                orderViewHolder.OrderItemName.setText(strOrderItemName);
                orderViewHolder.OrderItemPrice.setText(strOrderItemPrice);
                orderViewHolder.OrderMobNo.setText(strOrderMobNo);
                orderViewHolder.OrderMessName.setText(strCustName);
                orderViewHolder.tv_Qty.setText(strQty);
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(orderViewHolder.orderItemImg);
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    //used in customer module
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
                String strOrderMessMobNo = local.getMessPhone();
                String strOrderItemName = local.getMenu();
                String strOrderMessName = local.getMessName();
                String strCustName = local.getCustName();
                String strQty = local.getQuantity();
                Log.d(TAG, "inside loadOrders : strOrderId : " + strOrderId);
                Log.d(TAG, "inside loadOrders: strOrderItemPrice : " + strOrderItemPrice);
                Log.d(TAG, "inside loadOrders : strOrderMessMobNo : " + strOrderMessMobNo);
                Log.d(TAG, "inside loadOrders : strOrderItemName : " + strOrderItemName);
                Log.d(TAG, "inside loadOrders : strOrderMessName : " + strOrderMessName);
                Log.d(TAG, "inside loadOrders : strCustName : " + strCustName);
                Log.d(TAG, "inside loadOrders : strQty : " + strQty);
                orderViewHolder.OrderId.setText(strOrderId);
                orderViewHolder.OrderItemName.setText(strOrderItemName);
                orderViewHolder.OrderItemPrice.setText(strOrderItemPrice);
                orderViewHolder.OrderMobNo.setText(strOrderMessMobNo);
                orderViewHolder.OrderMessName.setText(strOrderMessName);
                orderViewHolder.tv_Qty.setText(strQty);
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(orderViewHolder.orderItemImg);

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}