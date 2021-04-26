package com.example.tyminiproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    private static final String TAG = "FoodList";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference foodList;
    StorageReference storageReference;
    String messId = "", str_Foodname;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    EditText etMenuName, etDesc, etPrice;
    Button btn_select, btn_upload;
    String foodId = "";
    Food newFood;
    Uri saveUri;
    private final int PICK_IMG_REQ = 71;
    ProgressBar progressBar;
    TextView tvNODATAFOUND;
    ImageView imgBackMyOrders ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        tvNODATAFOUND = findViewById(R.id.tvNOTFOUND);
        progressBar = findViewById(R.id.progressbar);

        imgBackMyOrders = findViewById(R.id.imgBackMyOrders);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            //used in customer module
            messId = getIntent().getStringExtra("MessId");//mess mob no

            Log.e(TAG, "inside onCreate : messId---" + messId);
            /*if (messId == null) {

                //used in owner module
                String strMessMob = getIntent().getStringExtra("strMessMob");
                Log.e(TAG, "inside onCreate : strMessMob---" + strMessMob);
                loadFoodList(strMessMob);
            }*/
        }

        foodList.child(messId).orderByChild("menuId").equalTo(messId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    tvNODATAFOUND.setVisibility(View.GONE);
                    loadFoodList(messId);
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
                Intent i = new Intent(FoodList.this, Home.class);
               startActivity(i);
               finish();
            }
        });

    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class, foodList.child(messId).orderByChild("menuId").equalTo(categoryId)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
                foodViewHolder.FoodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.FoodImage);

                final Food local = model;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + local.getName(), Toast.LENGTH_LONG).show();
                        String foodName = local.getName();
                        String foodPrice = local.getPrice();
                        String foodDesc = local.getDescription();
                        String foodDiscont = local.getDiscount();
                        String MessPhone = local.getMenuId();
                        Log.e(TAG, "inside loadFoodList : fname : " + foodName);
                        Log.e(TAG, "inside loadFoodList: fprice : " + foodPrice);
                        Log.e(TAG, "inside loadFoodList : fdiscount : " + foodDiscont);
                        Log.e(TAG, "inside loadFoodList : fdesc : " + foodDesc);
                        Intent i = new Intent(FoodList.this, FoodDetail.class);
                        i.putExtra("FoodId", adapter.getRef(position).getKey());

                        Log.e(TAG, "onClick: FoodId : " + adapter.getRef(position).getKey());
                        i.putExtra("MessPhone", MessPhone);
                        i.putExtra("customer", "customer");
                        startActivity(i);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}