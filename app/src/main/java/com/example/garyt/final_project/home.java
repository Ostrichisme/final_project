package com.example.garyt.final_project;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,DialogInterface.OnClickListener{
    int flag;
    long total; //total記錄總花費
    TextView account,cost;      //account紀錄帳本名稱  cost紀錄花費
    Spinner year,month,day;
    Toast tos;
    SQLiteDatabase db;
    Cursor cur;
    ListView lv;
    String dataSet;                   //接收帳本名稱
    String date;                      //計算日期字串
    static final String[] FROM=new String[] {"_id","date","item","dollar"};
    SimpleCursorAdapter adapter;
    String[] tempSet1={"1","2","3","4","5","6","7","8","9","10",             //大月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31"};
    String[] tempSet2={"1","2","3","4","5","6","7","8","9","10",             //小月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30"};
    String[] tempSet3={"1","2","3","4","5","6","7","8","9","10",             //2月
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        flag=0;
        account=(TextView)findViewById(R.id.account);
        cost=(TextView)findViewById(R.id.cost);
        lv=(ListView)findViewById(R.id.lv);
        tos=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        Intent it=getIntent();
        dataSet=it.getStringExtra("帳本");//帳本名稱
        account.setText(dataSet);
        db=openOrCreateDatabase("account", Context.MODE_PRIVATE,null); //帳戶資料庫
        String createTable="CREATE TABLE IF NOT EXISTS "+dataSet+            //帳本資料表
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                "date VARCHAR(40), " +
                "item VARCHAR(50)," +
                "dollar VARCHAR(10))";
        db.execSQL(createTable);
        year=(Spinner)findViewById(R.id.year);
        month=(Spinner)findViewById(R.id.month);
        month.setOnItemSelectedListener(this);//月要接收監聽
        day=(Spinner)findViewById(R.id.day);
        ArrayAdapter<String> tempAd=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempSet1);
        tempAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(tempAd);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //選好日期
    public void choose(View v) {
        date=year.getSelectedItem().toString()+"/"+month.getSelectedItem().toString()+"/"+day.getSelectedItem().toString();
        cur=db.rawQuery("SELECT * FROM "+dataSet+" WHERE date = ?", new String[] {date});
        adapter=new SimpleCursorAdapter(this, R.layout.layout,cur,FROM, new int[] {R.id.id,R.id.date,R.id.item,R.id.dollar},0);
        lv.setAdapter(adapter);
        if(cur.getCount()==0){
            cost.setText("查無資料");
        }
        else{
            cur.moveToFirst();
            total=0;
            do{
                total+=Integer.parseInt(cur.getString(cur.getColumnIndex("dollar")));
            }while(cur.moveToNext());
            cost.setText(String.valueOf(total));
        }

    }
    //新增
    public void add(View v) {
        date=year.getSelectedItem().toString()+"/"+month.getSelectedItem().toString()+"/"+day.getSelectedItem().toString();
        Intent it=new Intent(this,add.class);    //啟動add
        it.putExtra("帳本",dataSet);
        it.putExtra("日期",date);
        startActivity(it);
    }
    //編輯
    public void edit(View v) {
        Intent it=new Intent(this,edit.class);    //啟動edit
        it.putExtra("帳本",dataSet);
        startActivity(it);
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要登出嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
        return ;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which==DialogInterface.BUTTON_POSITIVE) {
            tos.setText("登出成功");
            tos.show();
            db.close();
            finish();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(flag==0){          //第一次進入home
            flag=1;
        }
        else if(flag==1){
            choose(null);
        }

    }
}
