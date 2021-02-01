package com.example.tyminiproject.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tyminiproject.Home;
import com.example.tyminiproject.Model.User;
import com.example.tyminiproject.R;
import com.example.tyminiproject.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    Button btn_signUp;

    EditText et_mob, et_pwd, et_name;
    String str_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signUp = findViewById(R.id.btn_signUp);
        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_pwd);
        et_name = findViewById(R.id.et_name);

        str_phone = getIntent().getStringExtra("mobno");
        et_mob.setText(str_phone);
        et_mob.setEnabled(false);
        Log.e(TAG, "onCreate: " + str_phone);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("user");

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            User newuser = new User(et_name.getText().toString(), et_pwd.getText().toString());
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
        });
    }

}