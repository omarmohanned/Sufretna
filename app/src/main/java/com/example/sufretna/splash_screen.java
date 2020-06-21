package com.example.sufretna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ProgressDialog progressDialog = new ProgressDialog(splash_screen.this);
        progressDialog.setMessage("Welcome To Our Sufretna :)");

        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                CountDownTimer a = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {
                        progressDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.cancel();
                        startActivity(new Intent(getApplicationContext(), sign_in.class));
                        finish();
                    }
                }.start();


            }
        }, 3000);
    }
}
