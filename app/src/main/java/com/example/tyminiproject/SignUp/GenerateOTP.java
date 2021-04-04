package com.example.tyminiproject.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tyminiproject.R;
import com.example.tyminiproject.SignIn;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GenerateOTP extends AppCompatActivity {
    String TAG = "GenerateOTP";
    Button btn_verifyOTP, btn_signUp, btn_getOtp;

    EditText et_mob, et_pwd, et_name, et_otp;
    String mobno, checkUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_o_t_p);
        et_mob = findViewById(R.id.et_mob1);
        btn_getOtp = findViewById(R.id.btn_getOTP);
        TextView t1 = findViewById(R.id.t1);
        ProgressBar progressBar = findViewById(R.id.progressbar);


        if (getIntent() != null)
            checkUserType = getIntent().getStringExtra("customer");
        Log.d(TAG, "inside onCreate : checkUserType---" + checkUserType);

        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobno = et_mob.getText().toString();
                if (mobno.isEmpty()) {
                    Toast.makeText(GenerateOTP.this, "empty", Toast.LENGTH_LONG).show();
                    et_mob.setError("please enter valid mobile no.");

                } else {
                    t1.setText(mobno);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + mobno,
                            60,
                            TimeUnit.SECONDS,
                            GenerateOTP.this,
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
                                    Toast.makeText(GenerateOTP.this, "inside onVerificationFailed : " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                                    intent.putExtra("mobile", mobno);
                                    intent.putExtra("verificationId", verificationId);
                                    intent.putExtra("customer","customer");
                                    startActivity(intent);
                                }
                            }
                    );


                    progressBar.setVisibility(View.VISIBLE);

                    Log.e(TAG, "onCreate: " + mobno);
                    Intent intent = new Intent(GenerateOTP.this, VerifyPhoneNo.class);
                    intent.putExtra("mobno", mobno);
                    intent.putExtra("customer","customer");
                    startActivity(intent);
                }
            }
        });
    }

    public void onCancel(View view) {
        Intent intent = new Intent(GenerateOTP.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}