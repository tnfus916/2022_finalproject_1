package com.example.a2022_finalproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class writeActivity extends AppCompatActivity {
    myDBHelper myDBHelper;
    DBHelper helper;
    EditText edtName, edtNumber, edtEvaluate ,edtQuantity, edtTime,edtDate;
    Button  btnInsert;
    SQLiteDatabase sqlDB;
    ImageView imageView;
    String imgName = "osz.png";    // 이미지 이름
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setTitle("식사 등록");

        this.InitializeView();
        this.InitializeListener();


        Button mainpagebutton1 = (Button) findViewById(R.id.button8);

        mainpagebutton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }

        });


        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        edtEvaluate = (EditText) findViewById(R.id.edtEvaluate);
        edtQuantity = (EditText) findViewById(R.id.edtQuantity);
        edtTime = (EditText) findViewById(R.id.edtTime);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        edtDate = (EditText) findViewById(R.id.textView_date);




/////////////
        myDBHelper = new myDBHelper(this);
        helper = new DBHelper(writeActivity.this, "newdb.db", null, 1);
        ///db생성
        /////////////////

 ///////////       /////////////
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                sqlDB = myDBHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ( '" + edtName.getText().toString() + "' , " + edtNumber.getText().toString() + "," + edtEvaluate.getText().toString() + ",'" + edtTime.getText().toString() + "'," + edtQuantity.getText().toString() + ",'" + edtDate.getText().toString() + "');");
                // 이름, 칼로리, 평가,시간, 수량,날짜
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "입력됨", 0).show();
            }
        });


///////////이미지관련


        imageView = findViewById(R.id.imageView2);

        try {
            String imgpath = getCacheDir() + "/" + imgName;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }

        Button btnMap=(Button) findViewById(R.id.button4);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent1);
            }
        });

        EditText editText = (EditText)findViewById(R.id.editText);

        Button getAdrs=(Button) findViewById(R.id.button7);
        getAdrs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String address = intent1.getStringExtra("주소");
                editText.setText(address);
            }
        });
    }




    public void bt1(View view) {    // 이미지 선택 누르면 실행됨 이미지 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData(); //--->>>db에 저장
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imageView.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기  sqlDB = myDBHelper.getWritableDatabase();
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void bt2(View view) {    // 이미지 삭제
        try {
            File file = getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // 배열의 크기만큼 반복
                if (flist[i].getName().equals(imgName)) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                    Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

    }

///////////////날짜 입력관련///////////
    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.textView_date);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()


        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2022, 11, 27);

        dialog.show();
    }



    }







