package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    int typeOfWeather = -1, typeOfMood = -1;
    boolean confirm = false;

    //rainy , sunny, windy, cloudy
    String [] weather = {"Rainy", "Sunny", "Windy", "Cloudy"};
    Button [] weather_btn = new Button[4];
    int [] weather_btnID = {R.id.btn_rainy, R.id.btn_sunny, R.id.btn_windy, R.id.btn_cloudy};

    //joyful, sad, upset, angry , disappointed, disgusted, excited
    String [] mood = {"Joyful", "Sad", "Upset", "Angry", "Disappointed", "Disgusted", "Excited"};
    Button [] mood_btn = new Button[7];
    int [] mood_btnID = {R.id.btn_joyful, R.id.btn_sad, R.id.btn_upset, R.id.btn_angry,
            R.id.btn_disappointed, R.id.btn_disgusted, R.id.btn_excited};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        for (int i = 0; i < weather_btn.length; i++) {
            weather_btn[i] = (Button) findViewById(weather_btnID[i]);
            weather_btn[i].setOnClickListener(this);
        }
        for (int i = 0; i < mood_btn.length; i++) {
            mood_btn[i] = (Button) findViewById(mood_btnID[i]);
            mood_btn[i].setOnClickListener(this);
        }

        Button btn_start = (Button) findViewById(R.id.btn_start);

        //判斷編輯or新建日記
        if (getIntent().hasExtra("天氣")) {  //編輯日記
            //根據傳遞的資訊初始化天氣＆心情選項
            for (int i = 0; i < weather.length; i++) {
                if (getIntent().getStringExtra("天氣").equals(weather[i])) {
                    typeOfWeather = i;
                    break;
                }
            }
            for (int i = 0; i < mood.length; i++) {
                if (getIntent().getStringExtra("心情").equals(mood[i])) {
                    typeOfMood = i;
                    break;
                }
            }
            confirm = true;
            btn_start.setText("Confirm");
            changeColor();
        }

        btn_start.setOnClickListener(view -> {
            if (confirm) {  //編輯日記，回傳改動資訊
                Intent it = new Intent(this, MainActivity5.class);
                it.putExtra("天氣", weather[typeOfWeather]);
                it.putExtra("心情", mood[typeOfMood]);
                setResult(2, it);
                finish();
            } else {  //新建日記，跳轉到創建日記頁面（內容）
                //提示使用者尚未選擇天氣＆心情
                if (typeOfWeather == -1) {
                    Toast.makeText(MainActivity3.this,"Choose a weather",Toast.LENGTH_SHORT).show();
                } else if (typeOfMood == -1) {
                    Toast.makeText(MainActivity3.this,"Choose your mood",Toast.LENGTH_SHORT).show();
                } else {
                    Intent it = new Intent(this, MainActivity4.class);
                    it.putExtra("日期", getIntent().getStringExtra("日期"));
                    it.putExtra("天氣", weather[typeOfWeather]);
                    it.putExtra("心情", mood[typeOfMood]);
                    startActivityForResult(it, 1);
                }
            }
        });
    }

    //從其他頁面回到選擇心情頁面的動作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Intent it = new Intent();
            //判斷日記內容是否為空
            if (!data.getStringExtra("text").matches("")) {  //不為空，將日記內容回傳到主頁面
                it.putExtra("天氣", weather[typeOfWeather]);
                it.putExtra("心情", mood[typeOfMood]);
                it.putExtra("text", data.getStringExtra("text"));
                setResult(1, it);
                finish();
            } else {
                //日記內容為空，維持在心情頁面
            }
        }
    }

    //監控返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent it = new Intent();
            it.putExtra("text", "");
            setResult(1, it);
            finish();
        }
        return true;
    }

    //判斷選擇的天氣＆心情
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_rainy) {
            typeOfWeather = 0;
        } else if (id == R.id.btn_sunny) {
            typeOfWeather = 1;
        } else if (id == R.id.btn_windy) {
            typeOfWeather = 2;
        } else if (id == R.id.btn_cloudy) {
            typeOfWeather = 3;
        } else if (id == R.id.btn_joyful) {
            typeOfMood = 0;
        } else if (id == R.id.btn_sad) {
            typeOfMood = 1;
        } else if (id == R.id.btn_upset) {
            typeOfMood = 2;
        } else if (id == R.id.btn_angry) {
            typeOfMood = 3;
        } else if (id == R.id.btn_disappointed) {
            typeOfMood = 4;
        } else if (id == R.id.btn_disgusted) {
            typeOfMood = 5;
        } else if (id == R.id.btn_excited) {
            typeOfMood = 6;
        }
        changeColor();
    }

    //改變按鈕顏色
    public void changeColor(){
        for (Button btn: weather_btn) {
            btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
        }
        for (Button btn: mood_btn) {
            btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
        }
        if (typeOfWeather != -1) {
            weather_btn[typeOfWeather].setBackgroundColor(getResources().getColor(R.color.purple_600));
        }
        if (typeOfMood != -1) {
            mood_btn[typeOfMood].setBackgroundColor(getResources().getColor(R.color.purple_600));
        }
    }
}