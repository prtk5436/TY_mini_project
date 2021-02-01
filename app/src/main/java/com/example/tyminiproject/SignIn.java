package com.example.tyminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tyminiproject.Model.User;
import com.example.tyminiproject.Common.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    Button login;
    EditText et_mob, et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.btn_login);
        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_pwd);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("user");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("please wait");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if user is present or not
                        if (snapshot.child(et_mob.getText().toString()).exists()) {
                            //get user info
                            progressDialog.dismiss();
                            User user = snapshot.child(et_mob.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(et_pwd.getText().toString())) {
                                Toast.makeText(SignIn.this, "Login Success", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "wrong id/password", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}