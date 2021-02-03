package com.example.tyminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tyminiproject.Model.Food;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    private static final String TAG ="FoodDetail" ;
    TextView foodName, foodPrice, foodDesc;
    ImageView foodImg;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference food  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDesc = findViewById(R.id.foodDescription);

        foodImg = findViewById(R.id.food_img);
        numberButton =findViewById(R.id.numberBtn);
        btnCart = findViewById(R.id.btn_cart);
        collapsingToolbarLayout=findViewById(R.id.collapsing);
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
                Food food = snapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(food.getImage()).into(foodImg);
                collapsingToolbarLayout.setTitle(food.getName()) ;
                foodPrice.setText(food.getPrice());
                foodDesc.setText(food.getDescription());
                foodName.setText(food.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}