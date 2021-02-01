package com.example.tyminiproject.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tyminiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.tasks.TaskExecutors.*;

public class VerifyPhoneNo extends AppCompatActivity {
    String TAG = "VerifyPhoneNo";
    String verificationId, phoneNo;
    Button btn_resendOTP, btn_verifyOTP, btn_signUp;
    EditText et_mob, et_pwd, et_name, et_otp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        progressBar = findViewById(R.id.progressbar);

        btn_resendOTP = findViewById(R.id.btn_resendOTP);
        btn_verifyOTP = findViewById(R.id.btn_verifyOTP);
        et_otp = findViewById(R.id.et_otp);

        phoneNo = getIntent().getStringExtra("mobile");
        Log.e(TAG, "onCreate: " + phoneNo);

        verificationId = getIntent().getStringExtra("verificationId");
        Log.e(TAG, "onCreate: " + verificationId);

        btn_verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_otp == null) {
                    Toast.makeText(getApplicationContext(), "enter OTP", Toast.LENGTH_LONG).show();
                    return;
                }
                String userOTP = et_otp.getText().toString();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    btn_verifyOTP.setVisibility(View.GONE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    btn_verifyOTP.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Verification completed", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobno", phoneNo);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "enter Valid OTP", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            }
        });


        btn_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNo,
                        60,
                        TimeUnit.SECONDS,
                        VerifyPhoneNo.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                btn_verifyOTP.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                               /* Log.e(TAG,"inside onVerificationFailed : " + e.getMessage());
                                Toast.makeText(GenerateOTP.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();
*/
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                btn_verifyOTP.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Log.e(TAG,"inside onVerificationFailed : " + e.getMessage());
                                Toast.makeText(VerifyPhoneNo.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String NEWverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId= NEWverificationId;
                                Toast.makeText(VerifyPhoneNo.this, "OTP sent  " , Toast.LENGTH_LONG).show();

                            }
                        }
                );
            }
        });
    }


}