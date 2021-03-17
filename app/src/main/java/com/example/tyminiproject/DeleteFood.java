package com.example.tyminiproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteFood extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference foodList = firebaseDatabase.getReference("Food");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_food);
        EditText etFname = findViewById(R.id.etFoodName);
        Button delete = findViewById(R.id.btnDeleteFood);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFname.getText().toString();
                if (fName.isEmpty()) {
                    Toast.makeText(DeleteFood.this, "please enter food name", Toast.LENGTH_LONG).show();
                } else {
                    foodList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(etFname.getText().toString()).exists()) {
                                foodList.child(fName).removeValue();
                                Toast.makeText(DeleteFood.this, "Food Deleted!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DeleteFood.this, "Food Not Found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}