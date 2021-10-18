package com.example.tyminiproject.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tyminiproject.Home;
import com.example.tyminiproject.MessOwnerSignIn;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessOwnerSignUp extends AppCompatActivity {
    private static final String TAG = "MessOwnerSignUp";

    Button btn_signUp;
    EditText et_mob, et_pwd, et_Cpwd, et_name, et_reg, et_address, et_owner, etAM, etPM, etoffDay;
    String str_phone, pwd, Cpwd, name, messReg, messAddr, str_ownerName, img, strTime, AM, PM, str_offDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_owner_sign_up);

        btn_signUp = findViewById(R.id.btn_MessSignUp);
        et_mob = findViewById(R.id.et_mob);
        et_pwd = findViewById(R.id.et_Messpwd);
        et_Cpwd = findViewById(R.id.et_MessCpwd);
        et_name = findViewById(R.id.et_messname);
        et_reg = findViewById(R.id.et_messReg);
        et_address = findViewById(R.id.et_messAddr);
        et_owner = findViewById(R.id.et_owner);
        etAM = findViewById(R.id.et_AM);
        etPM = findViewById(R.id.et_PM);
        etoffDay = findViewById(R.id.et_offDay);

        str_phone = getIntent().getStringExtra("mobileNo");
        et_mob.setText(str_phone);
        et_mob.setEnabled(false);
        Log.e(TAG, "onCreate: str_phone :" + str_phone);
        str_ownerName = getIntent().getStringExtra("custName");
        et_owner.setText(str_ownerName);
        et_mob.setEnabled(false);
        Log.e(TAG, "onCreate: str_ownerName :" + str_ownerName);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                pwd = et_pwd.getText().toString();
                Cpwd = et_Cpwd.getText().toString();
                messReg = et_reg.getText().toString();
                messAddr = et_address.getText().toString();
                AM = etAM.getText().toString();
                PM = etPM.getText().toString();
                str_offDay = etoffDay.getText().toString() + " Closed";
//                secureCode=edtsecureCode.getText().toString();
                if (name.isEmpty() || pwd.isEmpty() || pwd.length()<6 || Cpwd.isEmpty() || messReg.isEmpty() || messAddr.isEmpty() || AM.isEmpty() || PM.isEmpty() || str_offDay.isEmpty()) {
                    Toast.makeText(MessOwnerSignUp.this, "please enter valid details", Toast.LENGTH_LONG).show();

                } else if (!pwd.equals(Cpwd)) {
                    et_Cpwd.setError("Password not matched");
                } else {
                    Log.e(TAG, "inside onCreate : name : " + name);
                    Log.e(TAG, "inside onCreate : pwd : " + pwd);
                    Toast.makeText(MessOwnerSignUp.this, "password matched", Toast.LENGTH_LONG).show();
                    registerUser();
                }
            }
        });

    }

    private void registerUser() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference table_user = firebaseDatabase.getReference("MessUser");

        ProgressDialog progressDialog = new ProgressDialog(MessOwnerSignUp.this);
        progressDialog.setMessage("please wait");
        progressDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check if user is present or not
                if (snapshot.child(et_mob.getText().toString()).exists()) {
                    //get user info
                    progressDialog.dismiss();
                    Toast.makeText(MessOwnerSignUp.this, "phone no already registered", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();

                    strTime = AM + " AM  -  " + PM + " PM";
                    Log.e(TAG, "inside onDataChange : name : " + name);
                    Log.e(TAG, "inside onDataChange : pwd : " + pwd);
                    Log.e(TAG, "inside onDataChange : messReg : " + messReg);
                    Log.e(TAG, "inside onDataChange : messAddr : " + messAddr);
                    Log.e(TAG, "inside onDataChange : str_ownerName : " + str_ownerName);
                    Log.e(TAG, "inside onDataChange : strTime : " + strTime);
                    Log.e(TAG, "inside onDataChange : str_offDay : " + str_offDay);
                    img = "https://data.tibettravel.org/assets/images/Tibet-bhutan-tour/indian-food-in-Lhasa.webp";

                    MessUser newuser = new MessUser(img, str_ownerName, strTime, messAddr, name, pwd, messReg, str_offDay, str_phone);

                    table_user.child(str_phone).setValue(newuser);

                    FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                    DatabaseReference table_user1 = firebaseDatabase1.getReference("user");
                    table_user1.getRef().equalTo(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            table_user1.child(str_phone).child("messOwner").setValue("YES");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(MessOwnerSignUp.this, "Mess Registered Successfully!!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MessOwnerSignUp.this, MessOwnerSignIn.class);
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
        Intent intent = new Intent(MessOwnerSignUp.this, Home.class);
        startActivity(intent);
        finish();
    }
}