package com.example.tyminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.Model.User;
import com.example.tyminiproject.SignUp.GenerateOTP;
import com.example.tyminiproject.SignUp.MessOwnerSignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessOwnerSignIn extends AppCompatActivity {
    private static final String TAG = "MessOwnerSignIn";
    ImageButton login;
    EditText et_mob, et_pwd;
    LinearLayout linear_signUp;
    String mobno, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_owner_sign_in);
        login = findViewById(R.id.btn_MessLogin);
        et_mob = findViewById(R.id.et_MessMob);
        et_pwd = findViewById(R.id.et_Messpwd);
        linear_signUp = findViewById(R.id.linear_signUp);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("MessUser");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobno = et_mob.getText().toString();
                pwd = et_pwd.getText().toString();
                if (mobno.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(MessOwnerSignIn.this, "please enter valid id/password", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(MessOwnerSignIn.this);
                    progressDialog.setMessage("please wait");
                    progressDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if user is present or not
                            if (snapshot.child(et_mob.getText().toString()).exists()) {
                                //get user info
                                progressDialog.dismiss();
                                MessUser user = snapshot.child(et_mob.getText().toString()).getValue(MessUser.class);
                                user.setPhone(et_mob.getText().toString());
                                if (user.getPassword().equals(et_pwd.getText().toString())) {
                                    Toast.makeText(MessOwnerSignIn.this, "Login Success", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(MessOwnerSignIn.this, MessOwnerHome.class);
                                    Common.currentMessUser = user;
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(MessOwnerSignIn.this, "wrong id/password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MessOwnerSignIn.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

/*
        linear_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessOwnerSignIn.this, GenerateOTP.class);
                startActivity(i);
                finish();
            }
        });
*/
    }
    public void onCancel(View view) {
        Intent intent = new Intent(MessOwnerSignIn.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}