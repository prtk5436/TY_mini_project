package com.example.tyminiproject.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView FoodName;
    public ImageView FoodImage;
    public ItemClickListner itemClickListner;


    public FoodViewHolder(View itemView) {
        super(itemView);

        FoodName = itemView.findViewById(R.id.foodName);
        FoodImage = itemView.findViewById(R.id.food_img);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), true);
    }

}
