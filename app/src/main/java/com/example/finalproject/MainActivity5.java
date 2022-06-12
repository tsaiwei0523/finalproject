package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity5 extends AppCompatActivity {

    TextView txvDate;
    EditText diaryContent;
    Button btnWeather, btnMood, btnEdit, btnBack, btnDel;
    String content, weather, mood;


    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        txvDate = (TextView) findViewById(R.id.date_record_text);
        txvDate.setText(getIntent().getStringExtra("日期"));
        diaryContent = (EditText) findViewById(R.id.diary_content);
        diaryContent.setText("");
        btnWeather = (Button) findViewById(R.id.weather);
        btnMood = (Button) findViewById(R.id.mood);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnDel = (Button) findViewById(R.id.btn_del);

        //展示日記內容
        content = getIntent().getStringExtra("內容");
        weather = getIntent().getStringExtra("天氣");
        mood = getIntent().getStringExtra("心情");
        if (!content.equals("")) {
            btnWeather.setText(weather);
            btnMood.setText(mood);
            diaryContent.setText(content);
        }


        //編輯＆儲存日記功能
        btnEdit.setOnClickListener(view ->  {
            if (flag == 0) {  //點擊Edit，進入編輯模式
                btnEdit.setText("Save");
                btnBack.setText(getString(R.string.recover));
                //設定日記內容為可更改狀態
                diaryContent.setFocusable(true);
                diaryContent.setFocusableInTouchMode(true);
                diaryContent.requestFocus();
                flag = 1;
            } else {  //點擊Save，儲存編輯內容
                if (!diaryContent.getText().toString().matches("")) {
                    //儲存更新後的日記內容
                    content = diaryContent.getText().toString();
                    btnEdit.setText("Edit");
                    btnBack.setText(getString(R.string.left_arrow));
                    //設定日記內容為不可改動狀態
                    diaryContent.setFocusable(false);
                    diaryContent.setFocusableInTouchMode(false);
                    flag = 0;
                } else {  //日記內容為空，提示不可為空
                    Toast.makeText(MainActivity5.this,"Empty Content",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //返回＆取消編輯功能
        btnBack.setOnClickListener(view -> {
            if (flag == 0) {  //點擊返回，回傳改動資訊至主頁面
                if (!diaryContent.getText().toString().matches("")) {
                    Intent it = new Intent();
                    String text = diaryContent.getText().toString();
                    it.putExtra("天氣", weather);
                    it.putExtra("心情", mood);
                    it.putExtra("text", text);
                    setResult(2, it);
                    finish();
                }
            } else {  //點擊X，取消改動
                btnEdit.setText("Edit");
                btnBack.setText(getString(R.string.left_arrow));
                //設定日記內容為不可改動狀態
                diaryContent.setFocusable(false);
                diaryContent.setFocusableInTouchMode(false);
                flag = 0;
                //回復更改前的日記內容
                diaryContent.setText(content);
            }
        });

        //刪除日記
        btnDel.setOnClickListener(view -> {
            AlertDialog.Builder bdr = new AlertDialog.Builder(this);
            bdr.setTitle("Do you REALLY want to DELETE this Diary?");
            bdr.setMessage("The diary can not be recover after delete");
            bdr.setPositiveButton("Yes", (dialogInterface, i) -> {
                setResult(3);
                finish();
            });
            bdr.setNegativeButton("No", null);
            bdr.show();
        });
    }

    //跳轉到改動天氣＆心情頁面
    public void goToEdit(View view) {
        Intent it = new Intent(this, MainActivity3.class);
        it.putExtra("天氣", weather);
        it.putExtra("心情", mood);
        startActivityForResult(it, 2);
    }

    //從其他頁面回到編輯日記頁面的動作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {  //更改天氣＆心情
            weather = data.getStringExtra("天氣");
            mood = data.getStringExtra("心情");
            btnWeather.setText(weather);
            btnMood.setText(mood);
        }
    }

    //監控返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent it = new Intent();
            if (flag == 0) {  //閱覽狀態，回傳改動資訊至主頁面
                if (!diaryContent.getText().toString().matches("")) {
                    String text = diaryContent.getText().toString();
                    it.putExtra("天氣", weather);
                    it.putExtra("心情", mood);
                    it.putExtra("text", text);
                    setResult(2, it);
                    finish();
                }
            } else {  //編輯狀態，判定為取消編輯，回到主畫面
                it.putExtra("天氣", weather);
                it.putExtra("心情", mood);
                it.putExtra("text", "");
                setResult(2, it);
                finish();
            }
        }
        return true;
    }


}