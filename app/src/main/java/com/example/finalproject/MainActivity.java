package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView welcome_tv;
    public static int TimeOutSec = 3000;//ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome_tv = (TextView) findViewById(R.id.welcome);

        //splash
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this , MainActivity2.class);
                startActivity(i);
                finish();
            }
        },TimeOutSec);
    }
}
