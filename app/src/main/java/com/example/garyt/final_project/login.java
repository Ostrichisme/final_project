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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity
        implements DialogInterface.OnClickListener,TextWatcher {
    SQLiteDatabase db;
    Cursor cur;
    EditText account;   //輸入的帳本
    Toast tos;
    Button login,add_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account=(EditText) findViewById(R.id.account);
        account.addTextChangedListener(this);
        login=(Button) findViewById(R.id.login);
        add_account=(Button) findViewById(R.id.add_account);
        tos=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        db=openOrCreateDatabase("account", Context.MODE_PRIVATE,null); //帳戶資料庫
        //建立帳本資料表
        String createTable="CREATE TABLE IF NOT EXISTS person" +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                "name VARCHAR(10))";
        db.execSQL(createTable);
    }

    public void login(View v){
        if(account.getText().toString().equals("admin")){
            tos.setText("進入管理員模式");
            tos.show();
            Intent it=new Intent(this,manage.class);    //啟動manage
            startActivity(it);
            return;
        }
        cur=db.rawQuery("SELECT * FROM person WHERE name = ?", new String[] {account.getText().toString()});
        if(cur.getCount()==0)  {//沒找到帳本的話
            tos.setText("查無帳本");
            tos.show();
            account.setText("");
        }
        else {
            tos.setText("登入成功");
            tos.show();
            Intent it=new Intent(this,home.class);    //啟動home
            it.putExtra("帳本",account.getText().toString());
            startActivity(it);
        }
    }
    public void add(View v){
        {
            if(account.getText().toString().equals("admin")){
                tos.setText("此帳本已存在");
                tos.show();
                return;
            }
            cur=db.rawQuery("SELECT * FROM person WHERE name = ?", new String[] {account.getText().toString()});
            if(cur.getCount()==0) {     //沒有帳本名稱的話

                ContentValues cv= new ContentValues(1);
                cv.put("name",account.getText().toString());
                db.insert("person",null,cv);
                tos.setText("已建立新帳本");
                tos.show();
            }
            else {
                tos.setText("此帳本已存在");
                tos.show();
                account.setText("");
            }

        }

    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要離開嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
        return ;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {          //離開程式

        if(which==DialogInterface.BUTTON_POSITIVE) {
            finish();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(account.getText().toString().length()>0){
            login.setEnabled(true);
            add_account.setEnabled(true);
        }
        else {

            login.setEnabled(false);
            add_account.setEnabled(false);
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        account.setText("");
    }
}
