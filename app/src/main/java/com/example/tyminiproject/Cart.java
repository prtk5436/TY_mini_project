package com.example.tyminiproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Database.Database;
import com.example.tyminiproject.Model.Order;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.ViewHolder.CartAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {


    private static final String TAG = "Cart";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView tvTotal;
    Button btnPlaceOrder;
    List<Order> carts = new ArrayList<>();
    CartAdapter adapter;
    int total = 0;

    FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_cart);
        recyclerView = findViewById(R.id.listCart);

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        tvTotal = findViewById(R.id.tvtotal);
        fabDelete = findViewById(R.id.fabDelete);
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadFoodList();

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                loadFoodList();
                tvTotal.setText("0");
                //Intent i = new Intent(Cart.this, Cart.class);
              //  startActivity(i);
                Toast.makeText(Cart.this, "Cart is Cleaned", Toast.LENGTH_LONG).show();
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("enter your Address : ");
        final EditText editText = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        String phone = Common.currentUser.getPhone();
        Log.e(TAG, "inside showAlertDialog : " + phone);
        String name = Common.currentUser.getName();
        Log.e(TAG, "inside showAlertDialog : " + name);
        String price = String.valueOf(total);
        Log.e(TAG, "inside showAlertDialog : " + price);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        phone,
                        name,
                        editText.getText().toString(),
                        price,
                        carts
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you!!", Toast.LENGTH_LONG).show();
                finish();
            }


        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadFoodList() {
        carts = new Database(this).getCarts();
        adapter = new CartAdapter(carts, this);
        recyclerView.setAdapter(adapter);

        for (Order order : carts) {

            int intPrice = (Integer.parseInt(order.getPrice()));
            Log.e(TAG, "inside loadFoodList : intPrice : " + intPrice);
            int intQuantity = (Integer.parseInt(order.getQuantity()));
            Log.e(TAG, "inside loadFoodList : intQuantity : " + intQuantity);
            total += intPrice * intQuantity;
            Log.e(TAG, "inside loadFoodList : total : " + total);

        }
        // Locale locale = new Locale("en", "US");

        String strTtotal = String.valueOf(total);
        // NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        //tvTotal.setText(fmt.format(total));
        tvTotal.setText(strTtotal);
    }
}