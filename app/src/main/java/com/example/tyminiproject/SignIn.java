package com.example.tyminiproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Model.User;
import com.example.tyminiproject.SignUp.GenerateOTP;
import com.example.tyminiproject.sample.SplashScreenActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    Button login;
    EditText et_mob, et_pwd;
    LinearLayout linear_signUp;
    String mobno, pwd;

    String endUser = "CUSTOMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.btn_login);
        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_pwd);
        linear_signUp = findViewById(R.id.linear_signUp);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("user");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobno = et_mob.getText().toString();
                pwd = et_pwd.getText().toString();
                if (mobno.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(SignIn.this, "please enter valid id/password", Toast.LENGTH_LONG).show();
                } else {
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
                                user.setPhone(et_mob.getText().toString());
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
            }
        });

        linear_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, Onboarding1.class);
                i.putExtra("customer", "customer");
                startActivity(i);
                finish();
            }
        });
    }

    public void gotoMessModule(View view) {
        Intent i = new Intent(SignIn.this, MessOwnerSignIn.class);
        startActivity(i);
    }

    public void onForgotPassword(View view) {
        Intent i = new Intent(SignIn.this, ForgotPasswordGenerateOTP.class);
        i.putExtra("customer", "customer");
        startActivity(i);
    }

    public void goToHelp(View view) {

        Intent i = new Intent(SignIn.this, SplashScreenActivity.class);
        //i.putExtra("customer", "customer");
        startActivity(i);
    }
}