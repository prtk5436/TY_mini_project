package com.example.tyminiproject.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tyminiproject.Model.User;
import com.example.tyminiproject.R;
import com.example.tyminiproject.SignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    Button btn_signUp;
    EditText et_mob, et_pwd, et_Cpwd, et_name,edtsecureCode;
    String str_phone, pwd, Cpwd, name,secureCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signUp = findViewById(R.id.btn_signUp);
        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_pwd);
        et_Cpwd = findViewById(R.id.et_Cpwd);
        et_name = findViewById(R.id.et_name);
      //  edtsecureCode = findViewById(R.id.edtsecureCode);
        str_phone = getIntent().getStringExtra("mobno");
        et_mob.setText(str_phone);
        et_mob.setEnabled(false);
        Log.e(TAG, "onCreate: " + str_phone);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                pwd = et_pwd.getText().toString();
                Cpwd = et_Cpwd.getText().toString();
//                secureCode=edtsecureCode.getText().toString();
                if (name.isEmpty() || pwd.isEmpty() ||pwd.length()<6 || Cpwd.isEmpty()) {
                    Toast.makeText(SignUp.this, "please enter valid details", Toast.LENGTH_LONG).show();

                } else if (!pwd.equals(Cpwd)) {
                    et_Cpwd.setError("Password not matched");
                } else {
                    Log.e(TAG, "inside onDataChange : name : " + name);
                    Log.e(TAG, "inside onCreate : pwd : " + pwd);
                    Toast.makeText(SignUp.this, "password matched", Toast.LENGTH_LONG).show();
                    registerUser();
                }
            }
        });

    }

    private void registerUser() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("user");


        ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("please wait");
        progressDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check if user is present or not
                if (snapshot.child(et_mob.getText().toString()).exists()) {
                    //get user info
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "phone no already registered", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();

                    Log.e(TAG, "inside onDataChange : name : " + name);
                    Log.e(TAG, "inside onDataChange : pwd : " + pwd);
                    String MessOwner = "NO";
                    User newuser = new User(name, pwd,MessOwner);
                    table_user.child(str_phone).setValue(newuser);
                    Toast.makeText(SignUp.this, "user registered successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignUp.this, SignIn.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onCancel(View view) {
        Intent intent = new Intent(SignUp.this, GenerateOTP.class);
        startActivity(intent);
        finish();
    }
}