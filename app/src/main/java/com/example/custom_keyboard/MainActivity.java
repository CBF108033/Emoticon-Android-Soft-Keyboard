package com.example.custom_keyboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnLongClickListener {
    private GlobalVariable gv;
    Button comfirm;
    EditText edttxt1_1, edttxt1_2, edttxt1_3, edttxt1_4, edttxt1_5, edttxt2_1, edttxt2_2, edttxt2_3, edttxt2_4, edttxt2_5
            , edttxt3_1, edttxt3_2, edttxt3_3, edttxt3_4, edttxt3_5, edttxt4_1, edttxt4_2, edttxt4_3, edttxt4_4, edttxt4_5;
    String currentStateString="default_1";
    String[] content;
    EditText[] editTexts;
    Spinner spinnerState;
    TableLayout tableLayout;
    EditText edtBox;
    ImageView squareStyle;
    ImageView listStyle;
    Toast t;
    static final String db_name="sql_DB";
    static final String tb_name="sql_table";
    SQLiteDatabase db;
    Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = Toast.makeText(this,"", Toast.LENGTH_SHORT);
        gv = (GlobalVariable)getApplicationContext();
        //db.execSQL("DROP TABLE IF EXISTS '" + tb_name + "'");
        //db.execSQL("DROP TABLE IF EXISTS tb_name");  //刪除資料表
        //資料庫
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable = "create table if not exists " + tb_name + " (state VARCHAR(32) PRIMARY KEY, content VARCHAR(500))";
        db.execSQL(createTable);
        cur=db.rawQuery("select * from "+tb_name,null);
        //app初始化
        if(cur.getCount()==0){gv.init();}
        //if(!gv.GetAllState().contains("default_1") && !gv.GetAllState().contains("default_2")){gv.init();}

        spinnerState=(Spinner)findViewById(R.id.spinnerState);
        comfirm=(Button) findViewById(R.id.comfirm);
        tableLayout=(TableLayout) findViewById(R.id.tableLayout);
        edtBox=(EditText) findViewById(R.id.edtBox);
        squareStyle=(ImageView)findViewById(R.id.tableStyle);
        listStyle=(ImageView)findViewById(R.id.listStyle);
        //所有修改格
        edttxt1_1=(EditText) findViewById(R.id.key1_1);
        edttxt1_2=findViewById(R.id.key1_2);
        edttxt1_3=findViewById(R.id.key1_3);
        edttxt1_4=findViewById(R.id.key1_4);
        edttxt1_5=findViewById(R.id.key1_5);
        edttxt2_1=findViewById(R.id.key2_1);
        edttxt2_2=findViewById(R.id.key2_2);
        edttxt2_3=findViewById(R.id.key2_3);
        edttxt2_4=findViewById(R.id.key2_4);
        edttxt2_5=findViewById(R.id.key2_5);
        edttxt3_1=findViewById(R.id.key3_1);
        edttxt3_2=findViewById(R.id.key3_2);
        edttxt3_3=findViewById(R.id.key3_3);
        edttxt3_4=findViewById(R.id.key3_4);
        edttxt3_5=findViewById(R.id.key3_5);
        edttxt4_1=findViewById(R.id.key4_1);
        edttxt4_2=findViewById(R.id.key4_2);
        edttxt4_3=findViewById(R.id.key4_3);
        edttxt4_4=findViewById(R.id.key4_4);
        edttxt4_5=findViewById(R.id.key4_5);

        edttxt1_1.setOnLongClickListener(this);
        edttxt1_2.setOnLongClickListener(this);
        edttxt1_3.setOnLongClickListener(this);
        edttxt1_4.setOnLongClickListener(this);
        edttxt1_5.setOnLongClickListener(this);
        edttxt2_1.setOnLongClickListener(this);
        edttxt2_2.setOnLongClickListener(this);
        edttxt2_3.setOnLongClickListener(this);
        edttxt2_4.setOnLongClickListener(this);
        edttxt2_5.setOnLongClickListener(this);
        edttxt3_1.setOnLongClickListener(this);
        edttxt3_2.setOnLongClickListener(this);
        edttxt3_3.setOnLongClickListener(this);
        edttxt3_4.setOnLongClickListener(this);
        edttxt3_5.setOnLongClickListener(this);
        edttxt4_2.setOnLongClickListener(this);
        edttxt4_3.setOnLongClickListener(this);
        edttxt4_4.setOnLongClickListener(this);

        spinnerState.setOnItemSelectedListener(this);
        squareStyle.setOnClickListener(this);
        listStyle.setOnClickListener(this);

        //設定spinner內容
        ArrayAdapter<String> stateSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,gv.GetAllState());
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選項的選項樣式
        spinnerState.setAdapter(stateSpinnerAdapter);

        editTexts =new EditText[] {edttxt1_1,edttxt1_2,edttxt1_3,edttxt1_4,edttxt1_5,edttxt2_1,edttxt2_2,edttxt2_3,edttxt2_4,edttxt2_5
        ,edttxt3_1,edttxt3_2,edttxt3_3,edttxt3_4,edttxt3_5,edttxt4_1,edttxt4_2,edttxt4_3,edttxt4_4,edttxt4_5};
        getKeyboardContent();

    }
    public void getKeyboardContent(){
        //資料庫
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable = "create table if not exists " + tb_name + " (state VARCHAR(32) PRIMARY KEY, content VARCHAR(500))";
        db.execSQL(createTable);
        cur=db.rawQuery("select * from "+tb_name,null);
        //app初始化
        if(cur.getCount()==0){gv.init();}

        //設定list內容
        String str="";
        for(String s:gv.AllStateContext().get(currentStateString)){
            str+=s+"\n";
        }
        edtBox.setText(str);
        //設定table內容
        for (String key : gv.AllStateContext().keySet()) {
            if(key.equals(currentStateString)){
                content = gv.AllStateContext().get(key);
            }
        }
        int curIndex=0;
        for(EditText edt:editTexts){
            if(!edt.getText().toString().equals("Last") && !edt.getText().toString().equals("Next")){
                edt.setText(content[curIndex]);
                curIndex++;
            }else{
                edt.setEnabled(false);
                edt.setBackgroundColor(Color.parseColor("#252525"));
            }
        }
    }
    public void confirm(View v){
        String[] txtContent_list=new String[18];

        AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(this);
        alertDialog.setTitle("修改");
        alertDialog.setMessage("是否要進行鍵盤按鈕修改?");
        alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(tableLayout.getVisibility()==View.VISIBLE) {
                    int curIndex = 0;
                    for (EditText edt : editTexts) {
                        if (!edt.getText().toString().equals("Last") && !edt.getText().toString().equals("Next")) {
                            content[curIndex] = edt.getText().toString();
                            curIndex++;
                        }
                    }
                    gv.editStateContext(currentStateString, content);
                    /*MyInputMethodService Service=new MyInputMethodService();
                    Service.onCreateInputView();
                    Service.setInputView(Service.onCreateInputView());*/
                    t.setText("已修改");
                    t.show();
                }else{
                    //String msg = gv.addStateContent(themeName.getText().toString(), txtContent_list);
                    //if(msg!=null){ t.setText(msg); t.show(); }
                    //else {
                        ArrayList<String> txtContent = new ArrayList<>(Arrays.asList(edtBox.getText().toString().split("[\r\n]+")));
                        txtContent.remove("");
                        //製作18種顏文字，不滿18自動增加空白
                        if(txtContent.size()<18){
                            int fillUp = 18-txtContent.size();
                            for(int i=0; i<fillUp; i++){
                                txtContent.add("");
                            }
                        }else {
                            int overAmt=txtContent.size()-18;
                            for(int i=0; i<overAmt; i++){
                                txtContent.remove(-1);
                            }
                        }
                        //array轉list
                        for(int i=0; i<txtContent_list.length; i++){
                            txtContent_list[i]=txtContent.get(i);
                        }
                    //}
                        gv.editStateContext(currentStateString,txtContent_list);
                        getKeyboardContent();
                }
            }
        });
        alertDialog.setNeutralButton("否",(dialog, which) -> {
            t.setText("已取消修改");
            t.show();
        });
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    public void confirmCancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentStateString=gv.GetAllState().get(position);
        getKeyboardContent();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tableStyle:
                edtBox.setVisibility(View.INVISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
                getKeyboardContent();
                break;
            case R.id.listStyle:
                edtBox.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.INVISIBLE);
                getKeyboardContent();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.key1_1:
                edttxt1_1.selectAll();
            case R.id.key1_2:
                edttxt1_2.selectAll();
            case R.id.key1_3:
                edttxt1_3.selectAll();
            case R.id.key1_4:
                edttxt1_4.selectAll();
            case R.id.key1_5:
                edttxt1_5.selectAll();
            case R.id.key2_1:
                edttxt2_1.selectAll();
            case R.id.key2_2:
                edttxt2_2.selectAll();
            case R.id.key2_3:
                edttxt2_3.selectAll();
            case R.id.key2_4:
                edttxt2_4.selectAll();
            case R.id.key2_5:
                edttxt2_5.selectAll();
            case R.id.key3_1:
                edttxt3_1.selectAll();
            case R.id.key3_2:
                edttxt3_2.selectAll();
            case R.id.key3_3:
                edttxt3_3.selectAll();
            case R.id.key3_4:
                edttxt3_4.selectAll();
            case R.id.key3_5:
                edttxt3_5.selectAll();
            case R.id.key4_1:
                edttxt4_1.selectAll();
            case R.id.key4_2:
                edttxt4_2.selectAll();
            case R.id.key4_3:
                edttxt4_3.selectAll();
            case R.id.key4_4:
                edttxt4_4.selectAll();
            case R.id.key4_5:
                edttxt4_5.selectAll();
        }
        return false;
    }
}