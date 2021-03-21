package com.example.tyminiproject.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartItemName,cartItemPrice ,cartItemCount, tvCartMessName;
    public Button btnPlaceOrder;
    public ItemClickListner itemClickListner;


    public CartViewHolder(View itemView) {
        super(itemView);

        cartItemName = itemView.findViewById(R.id.cartItemName);
        cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
        cartItemCount = itemView.findViewById(R.id.cartItemCount);//quantity
        tvCartMessName = itemView.findViewById(R.id.tvCartMessName);
        btnPlaceOrder = itemView.findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(this);

    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), true);
    }

}