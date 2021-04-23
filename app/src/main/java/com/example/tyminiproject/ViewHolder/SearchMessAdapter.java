package com.example.tyminiproject.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.FoodList;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.R;

import java.util.ArrayList;

public class SearchMessAdapter extends RecyclerView.Adapter<SearchMessAdapter.MyViewHolder> {

    private static final String TAG = "SearchMessAdapter";
    ArrayList<MessUser> list;
    Context context;

    public SearchMessAdapter(Context context, ArrayList<MessUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_mess_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.MessName.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvOwmner.setText(list.get(position).getOwner());
        holder.tvOffday.setText(list.get(position).getOffDay());
//        holder.MenuImage.setImageResource(Integer.parseInt(list.get(position).getImage()));
        //       Picasso.with(context).load(list.get(position).getImage()).into(holder.MenuImage);

        holder.btn_ViewMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, FoodList.class);
                i.putExtra("MessId", list.get(position).getPhone());
                Log.e(TAG, "onClick: MessId : " + list.get(position).getPhone());
                context.startActivity(i);
                //      Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView MessName, tvTime, tvOwmner, tvOffday;
        public ImageView MenuImage;
        public ItemClickListner itemClickListner;
        public Button btn_ViewMess;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            MessName = itemView.findViewById(R.id.menuName);
            MenuImage = itemView.findViewById(R.id.menu_img);
            tvOwmner = itemView.findViewById(R.id.messOwnerName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvOffday = itemView.findViewById(R.id.tvoffDay);

            btn_ViewMess = itemView.findViewById(R.id.btn_ViewMess);

        }

    }


}
