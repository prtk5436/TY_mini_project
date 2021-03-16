package com.example.tyminiproject.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView FoodName;
    public ImageView FoodImage;
    public ItemClickListner itemClickListner;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        FoodName = itemView.findViewById(R.id.foodName);
        FoodImage = itemView.findViewById(R.id.food_img);

        itemView.setOnClickListener(this);
        //itemView.OnCreateContextMenuListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}
