package com.example.tyminiproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tyminiproject.Common.Common;
import com.squareup.picasso.Picasso;

public class MessProfile extends AppCompatActivity {

    String messName, messOwner, messMob, messAddr, messOFFday, messTime, messBanner;

    TextView tvmessName, tvmessOwner, tvmessMob, tvmessAddr, tvmessOFFday, tvmessTime;
    ImageView imgMessBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_profile);

        tvmessName = findViewById(R.id.tvMessName);
        tvmessOwner = findViewById(R.id.tvMessOwnerName);
        tvmessMob = findViewById(R.id.tvMessMob);
        tvmessAddr = findViewById(R.id.tvMessAddress);
        tvmessOFFday = findViewById(R.id.tvMessOFFday);
        tvmessTime = findViewById(R.id.tvMessTime);
        imgMessBanner = findViewById(R.id.imgMessBanner);

        messName = Common.currentMessUser.getName();
        messOwner = Common.currentMessUser.getOwner();
        messMob = Common.currentMessUser.getPhone();
        messAddr = Common.currentMessUser.getAddress();
        messOFFday = Common.currentMessUser.getOffDay();
        messTime = Common.currentMessUser.getTime();
        messBanner = Common.currentMessUser.getImage();

        tvmessName.setText(messName);
        tvmessOwner.setText(messOwner);
        tvmessMob.setText(messMob);
        tvmessAddr.setText(messAddr);
        tvmessOFFday.setText(messOFFday);
        tvmessTime.setText(messTime);
        Picasso.with(getBaseContext()).load(messBanner)
                .into(imgMessBanner);

    }

    public void onBack(View view) {
        Intent i = new Intent(MessProfile.this, MessOwnerHome.class);
        startActivity(i);
        finish();
    }
}