package com.example.a2022_finalproject_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class foodlist extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;

    String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        // 권한 요청
        ActivityCompat.requestPermissions(foodlist.this, permissions,  1);

        ListView listView =(ListView)findViewById(R.id.listview);

        helper = new DBHelper(foodlist.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        // 쿼리
        String sql = "select * from mytable;";
        Cursor c = db.rawQuery(sql, null);

        String[] strs = new String[]{"uri","txt"};
        int[] ints = new int[] {R.id.listview_img, R.id.listview_txt};

        MyCursorAdapter adapter = null;
        adapter = new MyCursorAdapter(listView.getContext(), R.layout.listview, c, strs, ints);
        listView.setAdapter(adapter);

    }
}