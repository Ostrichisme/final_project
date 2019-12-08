package com.example.garyt.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class add extends AppCompatActivity
    implements DialogInterface.OnClickListener{

    TextView account,date;
    EditText item,dollar;
    Toast tos;
    SQLiteDatabase db;
    Cursor cur;
    String str_account,str_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        tos=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        account=(TextView)findViewById(R.id.account) ;
        date=(TextView)findViewById(R.id.date) ;
        item=(EditText)findViewById(R.id.item);
        dollar=(EditText)findViewById(R.id.dollar);
        Intent it=getIntent();
        str_account=it.getStringExtra("帳本");
        str_date=it.getStringExtra("日期");
        account.setText(account.getText().toString()+str_account);//帳本名稱
        date.setText(date.getText().toString()+str_date);//日期
        db=openOrCreateDatabase("account", Context.MODE_PRIVATE,null); //帳戶資料庫

    }
    public void add(View v) {
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要新增嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which==DialogInterface.BUTTON_POSITIVE) {
            if(item.getText().toString().length()==0||dollar.getText().toString().length()==0){
                tos.setText("請輸入想新增的資料");
                tos.show();
                return;
            }
            ContentValues cv=new ContentValues(3);
            cv.put("date",str_date);
            cv.put("item",item.getText().toString());
            cv.put("dollar",String.valueOf(Integer.valueOf(dollar.getText().toString())));
            db.insert(str_account,null,cv);
            tos.setText("已新增一筆紀錄");
            tos.show();
            db.close();
            finish();
        }
    }
}
