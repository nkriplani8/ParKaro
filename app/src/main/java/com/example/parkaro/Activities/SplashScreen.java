package com.example.parkaro.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.parkaro.R;
import com.google.firebase.FirebaseApp;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseApp.initializeApp(getApplicationContext());

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        //switch to main activity
        final Intent intent;
        if (preferences.contains("isUserLogin")) {
            intent = new Intent(SplashScreen.this, Dashboard.class);
        } else {
            intent = new Intent(SplashScreen.this, MainActivity.class);
        }
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }



}