package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Cart;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewCart extends AppCompatActivity {


    private static final String TAG = "Cart";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference cart;
    StorageReference storageReference;
    TextView tvTotal;
    Button btnPlaceOrder;
    String custMob = "";
    int total = 0;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    String foodName, foodTotalPrice, cartId, MessName, foodQuantity, foodImg;
    ImageView fabDelete;

    String foodId = "", custName = "", MessPhone = "", phone = "";

    ProgressBar progressBar;
    TextView tvNODATAFOUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_view_cart);
        recyclerView = findViewById(R.id.listCart);

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        ImageView imgDeleteAllCart = findViewById(R.id.imgDeleteAllCart);
        ImageView imgBackCart = findViewById(R.id.imgBackCart);
        //fabDelete = findViewById(R.id.fabDelete);

        database = FirebaseDatabase.getInstance();
        cart = database.getReference("Cart");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        custName = Common.currentUser.getName();
        phone = Common.currentUser.getPhone();
        Log.e(TAG, "inside onCreate : custName---" + custName);
        Log.e(TAG, "inside onCreate : phone---" + phone);
        if (getIntent() != null)
            custMob = getIntent().getStringExtra("customerMob");
        Log.e(TAG, "inside onCreate : custMob---" + custMob);
       /* if (!custMob.isEmpty() && custMob != null) {
            loadCartList(custMob);
        }*/

        tvNODATAFOUND = findViewById(R.id.tvNOTFOUND);
        progressBar = findViewById(R.id.progressbar);
        cart.orderByChild("phone").equalTo(custMob).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    tvNODATAFOUND.setVisibility(View.GONE);
                    loadCartList(custMob);
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


        imgBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCart.this, Home.class);
                startActivity(i);
                finish();
            }
        });

        imgDeleteAllCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanMyCart();
                Toast.makeText(ViewCart.this, "Cart is Cleaned", Toast.LENGTH_LONG).show();

            }
        });
        /*
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(Cart.this, Cart.class);
                //  startActivity(i);

                cleanMyCart();
                Toast.makeText(ViewCart.this, "Cart is Cleaned", Toast.LENGTH_LONG).show();
                // loadCartList(custMob);
            }
        });*/


    }

    private void cleanMyCart() {
        DatabaseReference cart_table = FirebaseDatabase.getInstance().getReference("Cart");

        cart_table.orderByChild("phone").equalTo(custMob)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.i("Tag", "test1");

                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            Log.i("Tag", "test2");
                            String key = postsnapshot.getKey();
                            postsnapshot.getRef().removeValue();
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", databaseError.getMessage());
                    }

                });

    }

    private void loadCartList(String custMob) {
        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(Cart.class, R.layout.cart_item,
                CartViewHolder.class, cart.orderByChild("phone").equalTo(custMob)) {
            @Override
            protected void populateViewHolder(CartViewHolder cartViewHolder, Cart model, int i) {
                cartViewHolder.cartItemName.setText(model.getMenu());

                final Cart local = model;
                cartId = adapter.getRef(i).getKey();
                foodName = local.getMenu();
                foodTotalPrice = local.getTotalPrice();
                MessName = local.getMessName();
                foodQuantity = local.getQuantity();
                foodImg = local.getImage();
                MessPhone=local.getMessPhone();

                Log.d(TAG, "populateViewHolder: foodName: " + foodName);
                Log.d(TAG, "populateViewHolder: foodPrice: " + foodTotalPrice);
                Log.d(TAG, "populateViewHolder: MessName: " + MessName);
                Log.d(TAG, "populateViewHolder: foodQuantity: " + foodQuantity);
                Log.d(TAG, "populateViewHolder: foodImg: " + foodImg);
                Log.d(TAG, "populateViewHolder: MessPhone: " + MessPhone);

                cartViewHolder.cartItemName.setText(foodName);
                cartViewHolder.cartItemPrice.setText(foodTotalPrice);
                cartViewHolder.cartItemCount.setText(foodQuantity);
                cartViewHolder.tvCartMessName.setText(MessName);
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(cartViewHolder.cartItemImg);

                cartViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        placeNewOrder();
                    }
                });
                //orderViewHolder.OrderMessName.setText(strOrderMessName);
            }
        };


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void placeNewOrder() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference requests_table = firebaseDatabase.getReference("Requests");
        Request newOrder = new Request(MessName, foodName, foodTotalPrice, foodQuantity, custName, phone, foodImg, MessPhone);

        requests_table.child(String.valueOf(System.currentTimeMillis())).setValue(newOrder);

        Toast.makeText(ViewCart.this, "Thank you!!", Toast.LENGTH_LONG).show();
        deleteFromCart();

    }

    private void deleteFromCart() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference cart_table = firebaseDatabase.getReference("Cart");
        cart_table.child(cartId).removeValue();

        Toast.makeText(ViewCart.this, "Order Place Sucessfully!!", Toast.LENGTH_LONG).show();
        loadCartList(custMob);
    }
}