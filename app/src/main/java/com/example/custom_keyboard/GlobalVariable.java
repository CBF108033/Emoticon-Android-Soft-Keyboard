package com.example.custom_keyboard;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariable extends Application {
    private String name = "123";
    private String[] currentStateContext;
    private Map<String,String[]> allStateContext=new HashMap<>();
    private ArrayList<String> allState=new ArrayList<>();
    private String[] default_1=new String[]{"(͒˶´⚇`˵)͒", "(˶ ᵕ  ˶)", "・ࡇ・", "꒰◍⍢◍꒱۶", "(*꒦ິ꒳꒦ີ)", "ॱଳ͘", "◔̯◔", "ʕ⸝⸝⸝˙Ⱉ˙ʔ", "(ᐡ•͈ ·̫ •͈ᐡ )。", "(´-ωก`)", "⁽⁽ଘ( ˊᵕˋ )ଓ⁾⁾", "୧⍤⃝୧", "ᙏ̤̫ ", "ᙏ̤̫͚ ", "ꪔ̤̥ ", " ꪔ̤̮ ", "ꪔ̤̱","ꪔ̤̱"};
    private String[] default_2=new String[]{"( Ꙭ )", "•͈౿•͈", "･ ʖ ･", "̫͚ ( *´ސު｀*)", "ॱଳ͘    ", "• ·̭ •̥", "˙ỏ˙", "⍤", "ᐧ༚ᐧ", "(˶‾᷄ꈊ‾᷅˵)", "(⑉･̆-･̆⑉)", "(๑⃙⃘´༥`๑⃙⃘)。", " •͈౿•͈", "ᙏ̤̫͚", "( ´ސު｀" ,"( ¯ᒡ̱¯ )ง", " ܸܸ ꙭ̱ ܸܸ", "˘ᵕ˘"};
    private String[] default_3=new String[]{"( Ꙭ )", "•͈౿•͈", "･ ʖ ･", "̫͚ ( *´ސު｀*)", "ॱଳ͘    ", "• ·̭ •̥", "˙ỏ˙", "⍤", "ᐧ༚ᐧ", "(˶‾᷄ꈊ‾᷅˵)", "(⑉･̆-･̆⑉)", "(๑⃙⃘´༥`๑⃙⃘)。", " •͈౿•͈", "ᙏ̤̫͚", "( ´ސު｀" ,"( ¯ᒡ̱¯ )ง", " ܸܸ ꙭ̱ ܸܸ", "˘ᵕ˘"};
    private String[] default_4;
    private String[] default_5;

    private String[] title= {"default_1","default_2","default_3","default_4","default_5"};
    private String pre_default;

    static final String db_name="sql_DB";
    static final String tb_name="sql_table";
    SQLiteDatabase db;
    Cursor cur;

    //初始化
    public void init(){
        //need 18
        editStateContext("default_1",default_1);
        editStateContext("default_2",default_2);
        editStateContext("default_3",default_3);
        editStateContext("default_4",default_4);
        editStateContext("default_5",default_5);

    }
    public ArrayList<String> GetAllState(){
        allState.removeAll(allState);
        //資料庫
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable = "create table if not exists " + tb_name + " (state VARCHAR(32) PRIMARY KEY, content VARCHAR(500))";
        db.execSQL(createTable);
        cur=db.rawQuery("select state from "+tb_name,null);
        if(cur.moveToFirst()){
            do{
                String state=cur.getString(0);
                allState.add(state);
            }while (cur.moveToNext());
        }

        return allState;
    }
    public Map<String,String[]> AllStateContext(){
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        Cursor cursor;
        cursor=db.rawQuery("select * from "+tb_name,null);
        if(cursor.moveToFirst()){
            do{
                String state=cursor.getString(0);
                String context=cursor.getString(1);
                //if(!allState.contains(editState)){ allState.add(editState); } //allState陣列增加state
                ArrayList<String> c = new ArrayList<>(Arrays.asList(context.split("/r/n")));
                if(c.size()<18){
                    int fillUp = 18-c.size();
                    for(int i=0; i<fillUp; i++){
                        c.add("");
                    }
                }
                //array轉list
                String[] c_list=new String[18];
                for(int i=0; i<c_list.length; i++){
                    c_list[i]=c.get(i);
                }
                Map<String,String[]> add = new HashMap<String,String[]>(){};    //增加/修改此state內容
                add.put(state,c_list);
                allStateContext.putAll(add);
            }while (cursor.moveToNext());
        }

        return allStateContext;
    }
    public void editStateContext(String editState, String[] editContext){
        if(editContext==null){  //剛開始建立全為空字串的陣列
            editContext=new String[18];
            for(int i=0;i<18;i++){
                editContext[i]="";
            }
        }
        //資料庫
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable = "create table if not exists " + tb_name + " (state VARCHAR(32) PRIMARY KEY, content VARCHAR(500))";
        db.execSQL(createTable);
        //資料庫前處理
        pre_default="";
        for(int i=0; i<editContext.length; i++){
            if(i==editContext.length-1){
                pre_default+=editContext[i];
            }else{
                pre_default+=editContext[i]+"/r/n";
            }
        }
        //資料新增或修改
        //cur=db.rawQuery("select state from "+tb_name,null);
        if(GetAllState().size()<5){  //新增資料
            addData(editState, pre_default);
        }else{  //修改資料
            ContentValues cv = new ContentValues(2);
            cv.put("state", editState);
            cv.put("content", pre_default);
            db.update(tb_name,cv,"state='"+editState+"'",null);
        }
        //db.close();
    }
    public void addData(String state, String content){
        ContentValues cv = new ContentValues(2);
        cv.put("state", state);
        cv.put("content", content);
        db.insert(tb_name,null,cv);
    }
    public void setName(String name){
        this.name = name;
    }

}
