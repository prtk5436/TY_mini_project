package com.example.tyminiproject.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Order;
import com.example.tyminiproject.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvCartName, tvCartPrice, tvCartMessName;
    public ImageView imgCartItemCount;

    private ItemClickListner itemClickListner;

    public TextView getTvCartName() {
        return tvCartName;
    }

    public void setTvCartName(TextView tvCartName) {
        this.tvCartName = tvCartName;
    }

    public TextView getTvCartPrice() {
        return tvCartPrice;
    }

    public void setTvCartPrice(TextView tvCartPrice) {
        this.tvCartPrice = tvCartPrice;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        tvCartName = itemView.findViewById(R.id.cartItemName);
        tvCartPrice = itemView.findViewById(R.id.cartItemPrice);
        tvCartMessName = itemView.findViewById(R.id.tvCartMessName);
        imgCartItemCount = itemView.findViewById(R.id.cartItemCount);

    }

    @Override
    public void onClick(View v) {


    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private static final String TAG = "CartAdapter";
    private List<Order> listData = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable textDrawable = TextDrawable.builder().buildRound("" + listData.get(position).getQuantity(), Color.RED);
        holder.imgCartItemCount.setImageDrawable(textDrawable);
        Locale locale = new Locale("en", "US");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int intPrice = (Integer.parseInt(listData.get(position).getPrice()));
        Log.e(TAG, "inside onBindViewHolder : intPrice : " + intPrice);
        int intQuantity = (Integer.parseInt(listData.get(position).getQuantity()));
        Log.e(TAG, "inside onBindViewHolder : intPrice : " + intPrice);
        int intTotalPrice = intPrice * intQuantity;
        Log.e(TAG, "inside onBindViewHolder : intPrice : " + intPrice);
        holder.tvCartPrice.setText(format.format(intTotalPrice));
        holder.tvCartName.setText(listData.get(position).getProductName());
        holder.tvCartMessName.setText(listData.get(position).getMessName());
    }

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
