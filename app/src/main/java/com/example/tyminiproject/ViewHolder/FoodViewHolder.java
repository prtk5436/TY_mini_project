package com.example.tyminiproject.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView FoodName;
    public ImageView FoodImage;
Button btnDeleteFood;
    public ItemClickListner itemClickListner;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    public FoodViewHolder(View itemView) {
        super(itemView);

        btnDeleteFood = itemView.findViewById(R.id.btnDeleteFood);
        FoodName = itemView.findViewById(R.id.foodName);
        FoodImage = itemView.findViewById(R.id.food_img);

        itemView.setOnClickListener(this);
/*
        btnDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFood();
            }
        };*/
/*        new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle("Select the action ");
                menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
                menu.add(0, 1, getAdapterPosition(), Common.DELETE);
            }
        };*/
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), true);
    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }*/
}
