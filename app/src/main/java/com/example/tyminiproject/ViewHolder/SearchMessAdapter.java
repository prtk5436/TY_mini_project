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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.FoodList;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.MessProfile;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        Picasso.with(context).load(list.get(position).getImage())
                .into(holder.MenuImage);
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

        holder.btn_messInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messId = list.get(position).getPhone();
                Log.e(TAG, "onClick: MessId : " + list.get(position).getPhone());
                getMessInfo(messId);
            }
        });

    }

    private void getMessInfo(String messId) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_messUser = firebaseDatabase.getReference("MessUser");
        table_messUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check if user is present or not
                if (snapshot.child(messId).exists()) {
                    //get user info
                    //progressDialog.dismiss();
                    MessUser user = snapshot.child(messId).getValue(MessUser.class);
                    user.setPhone(messId);

                    Intent i = new Intent(context, MessProfile.class);
                    Common.currentMessUser = user;
                    i.putExtra("role", "customer");
                    context.startActivity(i);
                } else {
                    //progressDialog.dismiss();
                    Toast.makeText(context, "user not found", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "user not found");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        public ImageView btn_messInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            MessName = itemView.findViewById(R.id.menuName);
            MenuImage = itemView.findViewById(R.id.menu_img);
            tvOwmner = itemView.findViewById(R.id.messOwnerName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvOffday = itemView.findViewById(R.id.tvoffDay);

            btn_ViewMess = itemView.findViewById(R.id.btn_ViewMess);

            btn_messInfo = itemView.findViewById(R.id.btn_messInfo);
        }

    }


}
