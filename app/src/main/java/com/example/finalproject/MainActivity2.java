package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity implements CalendarView.OnDateChangeListener {// implements DatePickerDialog.OnDateSetListener {

    static final String DB_NAME = "DiaryDB", TB_NAME = "Diary";
    int yr, mn, dy, hr, min;
    String w_day, month;

    TextView clock, date;
    Button btnCreate;

    SQLiteDatabase diaryDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        date = (TextView) findViewById(R.id.date_time);
        clock = (TextView) findViewById(R.id.clock_time);
        btnCreate = (Button) findViewById(R.id.button);

        //DateChangeListener
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(this);

        //open database
        diaryDB = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        //Create table
        String createTable = "create table if not exists " + TB_NAME
                + "(_id Integer PRIMARY KEY,"
                + "weather varchar(16),"
                + "mood varchar(16),"
                + "content text"
                + ")";
        diaryDB.execSQL(createTable);
        //diaryDB.execSQL("drop table " + TB_NAME);

        getTime();

    }

    void getTime(){
        Calendar calendar = Calendar.getInstance();
        yr = calendar.get(Calendar.YEAR);
        dy = calendar.get(Calendar.DAY_OF_MONTH);
        mn = calendar.get(Calendar.MONTH) + 1;
        hr = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        get_weekday();
        get_month();
        date.setText(getString(R.string.string_date, yr, mn, dy));
        clock.setText(w_day);
        preview(yr, mn, dy);
    }

    public void get_month(){
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH) + 1;//???0??????
        if (m == 1) {
            month = "Jan";
        }else if (m == 2) {
            month = "Feb";
        } else if (m == 3) {
            month = "Mar";
        } else if (m == 4) {
            month = "Apr";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "Jun";
        } else if (m == 7) {
            month = "Jul";
        } else if (m == 8) {
            month = "Aug";
        }else if (m == 9) {
            month = "Sep";
        } else if (m == 10) {
            month = "Oct";
        } else if (m == 11) {
            month = "Nov";
        } else if (m == 12) {
            month = "Dec";
        }
    }

    public void get_weekday(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == 2) {
            w_day = "Mon";
        } else if (day == 3) {
            w_day = "Tue";
        } else if (day == 4) {
            w_day = "Wed";
        } else if (day == 5) {
            w_day = "Thu";
        } else if (day == 6) {
            w_day = "Fri";
        } else if (day == 7) {
            w_day = "Sat";
        } else if (day == 1) {
            w_day = "Sun";
        }
    }

    //???????????????or???????????????????????????????????????
    public void createDiary(View view) {
        String dateID = Integer.toString(yr) + mn + dy;
        Cursor cursor = diaryDB.rawQuery("Select * from " + TB_NAME + " where _id = " + dateID, null);
        String date = getString(R.string.string_date, yr, mn, dy);
        if (cursor.getCount() == 0) {  //???????????????
            Intent it = new Intent(this, MainActivity3.class);
            it.putExtra("??????", date);
            startActivityForResult(it, 1);
        } else {  //??????????????????
            Intent it = new Intent(this, MainActivity5.class);
            cursor.moveToFirst();
            it.putExtra("??????", date);
            if (!cursor.getString(3).equals("")) {
                String content = cursor.getString(3);
                it.putExtra("??????", cursor.getString(1));
                it.putExtra("??????", cursor.getString(2));
                it.putExtra("??????", content);
            }
            startActivityForResult(it, 2);
        }
        cursor.close();
    }

    //Option menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.feedback) {
            Uri uri = Uri.parse("https://forms.gle/BDzfnWGa5M1ihc45A");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        } else if (id == R.id.help) {
            Intent it = new Intent();
            it.setAction(Intent.ACTION_SENDTO);
            it.setData(Uri.parse("mailto:B0928022@cgu.edu.tw"));
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }

    //???????????????????????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dateID = Integer.toString(yr) + mn + dy;
        if (resultCode == 1) {  //??????????????????
            Cursor cursor = diaryDB.rawQuery("Select * from " + TB_NAME + " where _id = " + dateID, null);
            //???????????????????????????????????????
            if (cursor.getCount() == 0) {
                //???????????????????????????????????????
                if (!data.getStringExtra("text").matches("")) {  //????????????????????????????????????????????????
                    ContentValues cv = new ContentValues(4);
                    cv.put("_id", dateID);
                    cv.put("weather", data.getStringExtra("??????"));
                    cv.put("mood", data.getStringExtra("??????"));
                    cv.put("content", data.getStringExtra("text"));
                    diaryDB.insert(TB_NAME, null, cv);
                    Toast.makeText(MainActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                    preview(yr, mn, dy);
                } else {  //??????????????????????????????
                    Toast.makeText(MainActivity2.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity2.this, "Already Exist",
                        Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else if (resultCode == 2) {  //????????????????????????
            if (!data.getStringExtra("text").matches("")) {
                ContentValues cv = new ContentValues(3);
                cv.put("weather", data.getStringExtra("??????"));
                cv.put("mood", data.getStringExtra("??????"));
                cv.put("content", data.getStringExtra("text"));
                diaryDB.update(TB_NAME, cv, "_id=" + dateID, null);
                preview(yr, mn, dy);
            } else {  //??????????????????????????????
                Toast.makeText(MainActivity2.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == 3) {
            diaryDB.delete(TB_NAME, "_id=" + dateID, null);
            Toast.makeText(MainActivity2.this, "Delete Success", Toast.LENGTH_SHORT).show();
            preview(yr, mn, dy);
        }
    }

    //???????????????????????????
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        yr = year;
        mn = month + 1;
        dy = day;
        //?????????????????????????????????
        preview(yr, mn, dy);
    }

    //???????????????
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {}
        return true;
    }

    //??????????????????????????????
    private void preview(int year, int month, int day) {
        String dateID = Integer.toString(year) + month + day;
        TextView txv = (TextView) findViewById(R.id.preview);
        Cursor cursor = diaryDB.rawQuery("Select * from " + TB_NAME + " where _id = " + dateID, null);
        if (cursor.getCount() == 0) {  //???????????????????????????
            txv.setText("");
            btnCreate.setText(getString(R.string.plus));
        } else {  //???????????????????????????
            cursor.moveToFirst();
            if (!cursor.getString(2).equals("")) {
                txv.setText(cursor.getString(2));
            }
            btnCreate.setText(getString(R.string.right_arrow));
        }
        cursor.close();
    }
}


