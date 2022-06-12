package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity {

    TextView txv_situation,txv_situation2;
    EditText edit_diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        txv_situation = (TextView) findViewById(R.id.situation);
        edit_diary = (EditText) findViewById(R.id.diary_edit);

        //展示日期、天氣、心情
        String situation = getString(R.string.situation, getIntent().getStringExtra("日期"),
                getIntent().getStringExtra("天氣"), getIntent().getStringExtra("心情"));
        txv_situation.setText(situation);

    }

    //Save按鈕
    public void save(View view) {
        Intent it = new Intent();
        String content;
        //判斷EditText內容是否為空
        if (!edit_diary.getText().toString().matches("")) {
            content = edit_diary.getText().toString();
            it.putExtra("text", content);
            setResult(1, it);
            finish();
        } else {  //EditText為空，提示不可為空白
            Toast.makeText(MainActivity4.this,"Empty Content",Toast.LENGTH_SHORT).show();
        }
    }

    //Cancel按鈕
    public void cancel(View view) {
        Intent it = new Intent();
        it.putExtra("text", "");
        setResult(1, it);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}