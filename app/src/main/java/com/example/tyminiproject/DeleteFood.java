package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DeleteFood extends AppCompatActivity {

    private static final String TAG = "DeleteFood";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference foodList;
    StorageReference storageReference;
    String messId = "", str_Foodname;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_food);
        EditText etFname = findViewById(R.id.etFoodName);
        Button delete = findViewById(R.id.btnDeleteFood);


        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            messId = getIntent().getStringExtra("strMessMob");
            Log.e(TAG, "inside onCreate : messId---" + messId);
            if (!messId.isEmpty() && messId != null) {
                loadFoodList(messId);
                //  btndelete.setVisibility(View.GONE);
            }
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFname.getText().toString();
                if (fName.isEmpty()) {
                    Toast.makeText(DeleteFood.this, "please enter food name", Toast.LENGTH_LONG).show();
                } else {


                    /*
                    foodList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(etFname.getText().toString()).exists()) {
                                foodList.child(fName).removeValue();
                                Toast.makeText(DeleteFood.this, "Food Deleted!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DeleteFood.this, "Food Not Found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                }
            }
        });
    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class, foodList.orderByChild("menuId").equalTo(categoryId)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
                foodViewHolder.FoodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.FoodImage);

                final Food local = model;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(DeleteFood.this, "" + local.getName(), Toast.LENGTH_LONG).show();
                        String foodName = local.getName();
                        String foodPrice = local.getPrice();
                        String foodDesc = local.getDescription();
                        String foodDiscont = local.getDiscount();
                        Log.e(TAG, "inside loadFoodList : fname : " + foodName);
                        Log.e(TAG, "inside loadFoodList: fprice : " + foodPrice);
                        Log.e(TAG, "inside loadFoodList : fdiscount : " + foodDiscont);
                        Log.e(TAG, "inside loadFoodList : fdesc : " + foodDesc);
                       /* Intent i = new Intent(DeleteFood.this, FoodDetail.class);
                        i.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(i);*/
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