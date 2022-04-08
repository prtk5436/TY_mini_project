package com.example.tyminiproject.MessRecommendationApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.R;

import java.util.List;

public class RecommendMessAdaptor extends RecyclerView.Adapter<RecommendMessAdaptor.MyViewHolder> {
    private static final String TAG = "SearchMessAdapter";


    List<Mess> messList;
    Context context;

    public RecommendMessAdaptor(Context context, List<Mess> messList) {
        this.context = context;
        this.messList = messList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_mess_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.RMessName.setText(messList.get(position).getName());
        holder.RmessCat.setText(messList.get(position).getCategories());
    }

    @Override
    public int getItemCount() {
        return messList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView RMessName, RmessCat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            RMessName = itemView.findViewById(R.id.RmessName);
            RmessCat = itemView.findViewById(R.id.RmessCat);

        }

    }
}
