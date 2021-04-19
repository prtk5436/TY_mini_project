package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordGenerateOTPmessOwner extends AppCompatActivity {
    String TAG = "ForgotPasswordGenerateOTPmessOwner";
    Button btn_getOtp;
    int i = 1, j = 1;
    EditText et_mob, et_pwd, et_name, et_otp;
    String mobno, checkUserType;
    TextView t1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_generate_o_t_pmess_owner);

        et_mob = findViewById(R.id.et_mob1);
        btn_getOtp = findViewById(R.id.btn_getOTP);
        t1 = findViewById(R.id.t1);
        progressBar = findViewById(R.id.progressbar);


        if (getIntent() != null)
            checkUserType = getIntent().getStringExtra("messOwner");
        Log.d(TAG, "inside onCreate : checkUserType---" + checkUserType);

        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_mob.setEnabled(false);
                mobno = et_mob.getText().toString();

                if (mobno.isEmpty() || mobno.length() < 10) {
                    Toast.makeText(ForgotPasswordGenerateOTPmessOwner.this, "empty", Toast.LENGTH_LONG).show();
                    et_mob.setError("please enter valid mobile no.");
                    et_mob.setEnabled(true);
                } else {
                    FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                    DatabaseReference table_Messuser = firebaseDatabase1.getReference("MessUser");

                    table_Messuser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child(et_mob.getText().toString()).exists()) {

                                Log.e(TAG, "MOB NO REG IN : MessUser TABLE ");
                                j++;//2,3,4,.....
                                Log.e(TAG, "inside onDataChange : j : " + j);
                                et_mob.setEnabled(false);
                                int k = i + j; //1+2=3

                                Log.e(TAG, "i : " + i);
                                Log.e(TAG, "j : " + j);
                                Log.e(TAG, "K : " + k);
                                if (k == 3) {
                                    generateOTP();
                                }

                            } else {
                                Toast.makeText(ForgotPasswordGenerateOTPmessOwner.this, "phone not registered as Mess Owner", Toast.LENGTH_LONG).show();
                                et_mob.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            et_mob.setEnabled(true);
                        }
                    });


                }
            }
        });


    }

    private void generateOTP() {
        et_mob.setEnabled(false);

        et_mob.setEnabled(false);
        t1.setText(mobno);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobno,
                60,
                TimeUnit.SECONDS,
                ForgotPasswordGenerateOTPmessOwner.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        btn_getOtp.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                               /* Log.e(TAG,"inside onVerificationFailed : " + e.getMessage());
                                Toast.makeText(GenerateOTP.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();
*/
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        btn_getOtp.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "inside onVerificationFailed : " + e.getMessage());
                        Toast.makeText(ForgotPasswordGenerateOTPmessOwner.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Intent intent = new Intent(getApplicationContext(), ForgotPasswordVerifyOTP.class);
                        intent.putExtra("mobile", mobno);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("checkUserType", "messOwner");
                        startActivity(intent);
                    }
                }
        );


        progressBar.setVisibility(View.VISIBLE);

        Log.e(TAG, "onCreate: " + mobno);
        Intent intent = new Intent(ForgotPasswordGenerateOTPmessOwner.this, ForgotPasswordVerifyOTP.class);
        intent.putExtra("mobno", mobno);
        intent.putExtra("checkUserType", "messOwner");
        startActivity(intent);
    }


    public void onCancel(View view) {
        Intent intent = new Intent(ForgotPasswordGenerateOTPmessOwner.this, MessOwnerSignIn.class);
        startActivity(intent);
        finish();
    }
}