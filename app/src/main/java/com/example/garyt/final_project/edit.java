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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class edit extends AppCompatActivity
        implements DialogInterface.OnClickListener,AdapterView.OnItemSelectedListener{

    TextView account;          //帳本
    EditText item,dollar,id;
    Toast tos;
    SQLiteDatabase db;
    Cursor cur;
    Button edit,delete;
    String str_account,date;
    int flag;
    int change=1;
    Spinner year,month,day;
    String[] tempSet1={"1","2","3","4","5","6","7","8","9","10",             //大月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31"};
    String[] tempSet2={"1","2","3","4","5","6","7","8","9","10",             //小月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30"};
    String[] tempSet3={"1","2","3","4","5","6","7","8","9","10",             //2月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28"};
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        tos=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        account=(TextView)findViewById(R.id.account) ;
        id=(EditText)findViewById(R.id.id);
        item=(EditText)findViewById(R.id.item);
        dollar=(EditText)findViewById(R.id.dollar);
        edit=(Button)findViewById(R.id.edit);
        delete=(Button)findViewById(R.id.delete);
        Intent it=getIntent();
        str_account=it.getStringExtra("帳本");
        account.setText(account.getText().toString()+str_account);//帳本名稱
        db=openOrCreateDatabase("account", Context.MODE_PRIVATE,null); //帳戶資料庫
        year=(Spinner)findViewById(R.id.y);
        month=(Spinner)findViewById(R.id.m);
        month.setOnItemSelectedListener(this);//月要接收監聽
        day=(Spinner)findViewById(R.id.d);
        ArrayAdapter<String> tempAd=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempSet1);
        tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(tempAd);


    }
    public void choose(View v) {
        if(id.getText().toString().length()==0) {
            tos.setText("請輸入您想編輯的id");
            tos.show();
            edit.setEnabled(false);
            delete.setEnabled(false);
        }
        else{
            cur=db.rawQuery("SELECT * FROM "+str_account+" WHERE _id = ?", new String[] {id.getText().toString()});
            if(cur.getCount()==0){
                tos.setText("查無此id");
                tos.show();
                id.setText("");
                edit.setEnabled(false);
                delete.setEnabled(false);

            }
            else{
                cur.moveToFirst();
                item.setText(cur.getString(cur.getColumnIndex("item")));
                dollar.setText(cur.getString(cur.getColumnIndex("dollar")));
                edit.setEnabled(true);
                delete.setEnabled(true);
                change=0;
                 String []parts= cur.getString(cur.getColumnIndex("date")).split("/");
                 temp=parts[2];
                if (parts[0].equals("2018")){
                    year.setSelection(0);
                }
                else{
                    year.setSelection(1);
                }
                month.setSelection(Integer.parseInt(parts[1])-1);
            }

        }

    }
    public void edit(View v) {
        flag=0;
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要更改嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
    }
    public void delete(View v) {
        flag=1;
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要刪除嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which==DialogInterface.BUTTON_POSITIVE) {
            if(flag==0){
                if(item.getText().toString().length()==0||dollar.getText().toString().length()==0){
                    tos.setText("請輸入想更改的資料");
                    tos.show();
                    return;
                }
                ContentValues cv=new ContentValues(3);
                date=year.getSelectedItem().toString()+"/"+month.getSelectedItem().toString()+"/"+day.getSelectedItem().toString();
                cv.put("date",date);
                cv.put("item",item.getText().toString());
                cv.put("dollar",String.valueOf(Integer.valueOf(dollar.getText().toString())));
                db.update(str_account,cv,"_id="+id.getText().toString(),null);
                tos.setText("修改成功");
                tos.show();
            }
            else if(flag==1){
                db.delete(str_account,"_id="+id.getText().toString(),null);
                tos.setText("已刪除一筆資料");
                tos.show();
            }
            db.close();
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {                    //月的spinner被按下
        //31天
        if(position==0||position==2||position==4||position==6||position==7||position==9||position==11){
            ArrayAdapter<String> tempAd=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempSet1);
            tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            day.setAdapter(tempAd);
        }
        //30天
        else if(position==2||position==3||position==5||position==8||position==10){
            ArrayAdapter<String> tempAd=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempSet2);
            tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            day.setAdapter(tempAd);
        }
        //28天
        else{
            ArrayAdapter<String> tempAd=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempSet3);
            tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            day.setAdapter(tempAd);
        }
        if(change==0) {
            change = 1;
            day.setSelection(Integer.parseInt(temp) - 1);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

