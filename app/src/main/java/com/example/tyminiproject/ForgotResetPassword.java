package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ForgotResetPassword extends AppCompatActivity {

    private static final String TAG = "ForgotResetPassword";
    String str_phone, pwd, Cpwd, checkUserType;
    EditText et_mob, et_pwd, et_Cpwd;
    Button btn_resetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_reset_password);

        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_pwd);
        et_Cpwd = findViewById(R.id.et_Cpwd);

        btn_resetPwd = findViewById(R.id.btn_resetPwd);


        if (getIntent() != null) {

            str_phone = getIntent().getStringExtra("mobno");
            checkUserType = getIntent().getStringExtra("customer");
            et_mob.setText(str_phone);
            et_mob.setEnabled(false);
        }

        Log.d(TAG, "inside onCreate: str_phone : " + str_phone);
        Log.d(TAG, "inside onCreate : checkUserType---" + checkUserType);
        /*
        str_ownerName = getIntent().getStringExtra("custName");
        et_owner.setText(str_ownerName);
        Log.e(TAG, "onCreate: str_ownerName :" + str_ownerName);*/

        btn_resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = et_pwd.getText().toString();
                Cpwd = et_Cpwd.getText().toString();
                if (pwd.isEmpty() || Cpwd.isEmpty()) {
                    Toast.makeText(ForgotResetPassword.this, "please enter valid password", Toast.LENGTH_LONG).show();

                } else if (!pwd.equals(Cpwd)) {
                    et_Cpwd.setError("Password not matched");
                } else {
                    Log.e(TAG, "inside onCreate : phone : " + str_phone);
                    Log.e(TAG, "inside onCreate : pwd : " + pwd);
                    Toast.makeText(ForgotResetPassword.this, "password matched", Toast.LENGTH_LONG).show();
                    if (checkUserType == null) {

                        ResetPasswordForMessOwner();

                    } else {

                        ResetPassword();
                        Intent i = new Intent(ForgotResetPassword.this, SignIn.class);
                        startActivity(i);
                        finish();
                    }


                }
            }
        });


    }

    private void ResetPasswordForMessOwner() {

        Log.e(TAG, "inside ResetPasswordForMessOwner : pwd : " + pwd);
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference table_MessUser = firebaseDatabase1.getReference("MessUser");

        table_MessUser.getRef().equalTo(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "inside ResetPasswordForMessOwner onDataChange : pwd : " + pwd);
                table_MessUser.child(str_phone).child("password").setValue(pwd);
                Toast.makeText(ForgotResetPassword.this, "Password Updated Successfully!!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ForgotResetPassword.this, MessOwnerSignIn.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void ResetPassword() {

        Log.e(TAG, "inside ResetPassword : pwd : " + pwd);
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference table_user1 = firebaseDatabase1.getReference("user");

        table_user1.getRef().equalTo(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "inside ResetPassword onDataChange : pwd : " + pwd);
                table_user1.child(str_phone).child("password").setValue(pwd);
                Toast.makeText(ForgotResetPassword.this, "Password Updated Successfully!!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}