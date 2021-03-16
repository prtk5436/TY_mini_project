package com.example.tyminiproject.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.R;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView MessName, tvTime, tvOwmner;
    public ImageView MenuImage;
    public ItemClickListner itemClickListner;
    public Button btn_ViewMess;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        MessName = itemView.findViewById(R.id.menuName);
        MenuImage = itemView.findViewById(R.id.menu_img);
        tvOwmner = itemView.findViewById(R.id.messOwnerName);
        tvTime = itemView.findViewById(R.id.tvTime);
        btn_ViewMess = itemView.findViewById(R.id.btn_ViewMess);
        btn_ViewMess.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
    }
}
