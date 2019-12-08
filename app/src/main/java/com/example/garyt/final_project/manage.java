package com.example.garyt.final_project;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class manage extends AppCompatActivity
    implements AdapterView.OnItemLongClickListener,DialogInterface.OnClickListener{

    int pos,flag;             //pos記錄點選的帳本在資料表的位置
    Toast tos;
    TextView count;          //count記錄有幾個帳本
    ListView lv;
    static final String[] FROM=new String[] {"name"};
    SQLiteDatabase db;
    Cursor cur;
    SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        count=(TextView)findViewById(R.id.count);
        lv=(ListView)findViewById(R.id.dynamic);
        tos=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        db=openOrCreateDatabase("account", Context.MODE_PRIVATE,null); //帳戶資料庫
        cur=db.rawQuery("SELECT * FROM person",null);
        adapter=new SimpleCursorAdapter(this, R.layout.account,cur,FROM, new int[] {R.id.name},0);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);
        count.setText(String.valueOf(cur.getCount()));

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        flag=0;
        pos=position;
        new AlertDialog.Builder(this).
                setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("確定要刪除嗎?")
                .setPositiveButton("是",this)
                .setNegativeButton("否",this)
                .show();
        return true;
    }
    @Override
    public void onBackPressed(){
        flag=1;
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
            if(flag==0){
                cur.moveToPosition(pos);
                db.execSQL("DROP TABLE IF EXISTS "+cur.getString(cur.getColumnIndex("name")));
                db.delete("person","_id="+cur.getInt(0),null);
                tos.setText("已刪除一筆帳本");
                tos.show();
                cur=db.rawQuery("SELECT * FROM person",null);
                adapter.changeCursor(cur);
                count.setText(String.valueOf(cur.getCount()));
            }
            else if(flag==1){
                db.close();
                tos.setText("登出成功");
                tos.show();
                finish();
            }

        }
    }

}
