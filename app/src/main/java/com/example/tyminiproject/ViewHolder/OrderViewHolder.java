package com.example.tyminiproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView OrderItemName, OrderMessName, OrderItemPrice, OrderMobNo, OrderId;
    public ImageView orderFood_img;
    public ItemClickListner itemClickListner;


    public OrderViewHolder(View itemView) {
        super(itemView);

        OrderItemName = itemView.findViewById(R.id.OrderItemName);
        OrderMessName = itemView.findViewById(R.id.OrderMessName);
        OrderItemPrice = itemView.findViewById(R.id.OrderItemPrice);
        OrderMobNo = itemView.findViewById(R.id.orderMobNo);
        OrderId = itemView.findViewById(R.id.orderId);
        orderFood_img = itemView.findViewById(R.id.orderFood_img);

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