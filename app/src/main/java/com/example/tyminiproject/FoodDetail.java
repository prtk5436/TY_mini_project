package com.example.tyminiproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Model.Cart;
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
    private static final String TAG = "FoodDetail";
    TextView foodName, foodPrice, foodDesc;
    ImageView foodImg;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnCart;
    Button numberButton;

    String foodId = "", custName = "", phone = "", MessPhone = "";
    FirebaseDatabase database;
    DatabaseReference food;
    String checkUserType=null;
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


        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
            MessPhone = getIntent().getStringExtra("MessPhone");
            checkUserType= getIntent().getStringExtra("customer");
            if (checkUserType ==null){
                btnCart.setVisibility(View.GONE);
            }
        }
        Log.e(TAG, "inside onCreate : foodId---" + foodId);
        Log.e(TAG, "inside onCreate : MessPhone---" + MessPhone);
        if (!foodId.isEmpty() && foodId != null) {
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(String foodId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("Cart");

        food.child(MessPhone).child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(foodImg);
          /*      String foodName=currentFood.getName();
                ;*/
                collapsingToolbarLayout.setTitle(currentFood.getMessName());
                foodPrice.setText(currentFood.getPrice());
                foodDesc.setText(currentFood.getDescription());
                foodName.setText(currentFood.getName());
                Log.e(TAG, "inside   getFoodDetails :fname :  " + currentFood.getName());
                Log.e(TAG, "inside   getFoodDetails :fprice :  " + currentFood.getPrice());
                Log.e(TAG, "inside   getFoodDetails :fdiscount : " + currentFood.getDiscount());
                Log.e(TAG, "inside   getFoodDetails : fdesc : " + currentFood.getDescription());
                Log.e(TAG, "inside   getFoodDetails : fMessNamee : " + currentFood.getMessName());


                btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        custName = Common.currentUser.getName();
                        phone = Common.currentUser.getPhone();

                        Log.e(TAG, "inside onCreate : custName---" + custName);
                        Log.e(TAG, "inside onCreate : phone---" + phone);

                        String foodMessName = currentFood.getMessName();
                        String foodName = currentFood.getName();
                        String foodQuantity = "5";
                        String foodPrice = currentFood.getPrice();
                        String foodImg = currentFood.getImage();
                        int totPrice = Integer.parseInt(foodPrice) * Integer.parseInt(foodQuantity);
                        String strTotPrice = String.valueOf(totPrice);

                        Log.e(TAG, "inside onCreate :foodMessName " + currentFood.getMessName());
                        Log.e(TAG, "inside onCreate :foodName " + currentFood.getName());
                        Log.e(TAG, "inside onCreate :foodQuantity " + numberButton.getText());
                        Log.e(TAG, "inside onCreate :foodPrice " + currentFood.getPrice());
                        Log.e(TAG, "inside onCreate :foodImg " + foodImg);
                        Log.e(TAG, "inside onCreate :MessPhone " + MessPhone);
                        Log.e(TAG, "inside onCreate : strTotPrice : " + strTotPrice);
                        Cart newCartItem = new Cart(foodMessName, foodName, strTotPrice, foodQuantity, custName, phone, foodImg, MessPhone);

                        table_user.child(String.valueOf(System.currentTimeMillis())).setValue(newCartItem);
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