package com.example.a2022_finalproject_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class page2 extends AppCompatActivity {
    myDBHelper myDBHelper;
    EditText edtNameResultm, edtNumberResult, edtTimeResult,edtQuantutyResult,edtEvaluateResult,edtDateresult;//이름, 칼로리, 평가,시간, 수량
    Button btnInit,  btnSelect;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        setTitle("식사 조회");

        edtTimeResult = (EditText) findViewById(R.id.edtTime);
        edtQuantutyResult = (EditText) findViewById(R.id.edtQuantity);
        edtEvaluateResult = (EditText) findViewById(R.id.edtEvaluate);
        edtDateresult= (EditText) findViewById(R.id.edtDate); //?

        edtNameResultm = (EditText) findViewById(R.id.edtNameResult);
        edtNumberResult = (EditText) findViewById(R.id.edtNumberResult);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnSelect = (Button) findViewById(R.id.btnSelect);


        myDBHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDBHelper.getWritableDatabase();
                myDBHelper.onUpgrade(sqlDB,1,2);
                sqlDB.close();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);

                String strNames = "음식이름"+"\r\n"+"\r\n";
                String strNumbers = "칼로리"+"\r\n"+"\r\n";
                String strEvaluate = "평가"+"\r\n"+"\r\n";
                String strTime = "시간"+"\r\n"+"\r\n";
                String strQuantity = "수량"+"\r\n"+"\r\n";
                String strDate="날짜"+"\r\n"+"\r\n";

                while (cursor.moveToNext()){
                    strNames += cursor.getString(0) + "\r\n";
                    strNumbers += cursor.getString(1) + "\r\n";
                    strEvaluate += cursor.getString(2) + "\r\n";
                    strTime += cursor.getString(3) + "\r\n";
                    strQuantity+= cursor.getString(4) + "\r\n";
                    strDate+= cursor.getString(5) + "\r\n";
                }
                edtNameResultm.setText(strNames);
                edtNumberResult.setText(strNumbers);
                edtEvaluateResult.setText(strEvaluate);
                edtTimeResult.setText(strTime);
                edtQuantutyResult.setText(strQuantity);
                edtDateresult.setText(strDate);
                cursor.close();
                sqlDB.close();
            }
        });




        ///////////////////////
        Button mainpagebutton1 = (Button) findViewById(R.id.button5);

        mainpagebutton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }

        });

        CalendarView calenderView=(CalendarView) findViewById(R.id.calendarView);
        TextView dateText=(TextView) findViewById(R.id.textView6);
        TextView showContent=(TextView) findViewById(R.id.textView7);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                dateText.setVisibility(View.VISIBLE);
                showContent.setVisibility(View.VISIBLE);
                dateText.setText(String.format("%d/%d/%d",year,month+1,dayOfMonth));
                showContent.setText("음식리스트");
            }
        });
    }
}

