package com.example.tyminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tyminiproject.Database.Database;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    private static final String TAG = "FoodDetail";
    TextView foodName, foodPrice, foodDesc;
    ImageView foodImg;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference food;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        btnCart = findViewById(R.id.btn_cart);

        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDesc = findViewById(R.id.foodDescription);

        foodImg = findViewById(R.id.food_img);
        numberButton = findViewById(R.id.numberBtn);
        btnCart = findViewById(R.id.btn_cart);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");


        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        Log.e(TAG, "inside onCreate : foodId---" + foodId);
        if (!foodId.isEmpty() && foodId != null) {
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(foodImg);
          /*      String foodName=currentFood.getName();
                ;*/
                collapsingToolbarLayout.setTitle(currentFood.getName());
                foodPrice.setText(currentFood.getPrice());
                foodDesc.setText(currentFood.getDescription());
                foodName.setText(currentFood.getName());
                Log.e(TAG, "inside   getFoodDetails :fname :  " + currentFood.getName());
                Log.e(TAG, "inside   getFoodDetails :fprice :  " + currentFood.getPrice());
                Log.e(TAG, "inside   getFoodDetails :fdiscount : " + currentFood.getDiscount());
                Log.e(TAG, "inside   getFoodDetails : fdesc : " + currentFood.getDescription());
                String foodPrice = currentFood.getPrice();
                String foodDesc = currentFood.getDescription();
                String foodDiscont = currentFood.getDiscount();

                btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "inside onCreate : " + currentFood.getName());
                        Log.e(TAG, "inside onCreate : " + currentFood.getPrice());
                        Log.e(TAG, "inside onCreate : " + currentFood.getDiscount());
                        Log.e(TAG, "inside onCreate : " + numberButton.getNumber());
                        new Database(getBaseContext()).addToCart(new Order(
                                foodId,
                                currentFood.getName(),
                                numberButton.getNumber(), ///Qunatity
                                currentFood.getPrice(),
                                currentFood.getDiscount()
                        ));

                        Toast.makeText(FoodDetail.this, "Added to Cart : " + currentFood.getName(), Toast.LENGTH_LONG).
                                show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}