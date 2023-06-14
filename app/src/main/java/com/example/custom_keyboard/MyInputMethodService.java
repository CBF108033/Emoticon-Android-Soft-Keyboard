package com.example.custom_keyboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{
    private GlobalVariable gv;
    private String currentState="default_1";
    private int currentStatePage=0;
    private String[] currentStateList;
    String[] toolKey =new String[]{"Next","Last", "DELETE", "SPACE", "ENTER"};
    Map<String,Keyboard> keyboard = new HashMap();

    private KeyboardView keyboardView;
    private Keyboard k1,k2,k3,k4,k5;    // 键盘1.2.3
    private ArrayList<Keyboard> keyboard_layout=new ArrayList<>();

    static final String db_name="sql_DB";
    static final String tb_name="sql_table";
    SQLiteDatabase db;
    Cursor cur;
    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        k1 = new Keyboard(this, R.xml.number_pad);
        k2 = new Keyboard(this, R.xml.number_pad_2);
        k3 = new Keyboard(this, R.xml.number_pad_3);
        k4 = new Keyboard(this, R.xml.number_pad_4);
        k5 = new Keyboard(this, R.xml.number_pad_5);
        keyboard_layout.add(k1);
        keyboard_layout.add(k2);
        keyboard_layout.add(k3);
        keyboard_layout.add(k4);
        keyboard_layout.add(k5);
        //keyboardView.setKeyboard(keyboard_layout.get(currentStatePage));
        keyboardView.setOnKeyboardActionListener(this);
        gv = (GlobalVariable)getApplicationContext();
        //keyboard.put(state[0],k1);
        //keyboard.put(state[1],k2);

        //資料庫
        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable = "create table if not exists " + tb_name + " (state VARCHAR(32) PRIMARY KEY, content VARCHAR(500))";
        db.execSQL(createTable);
        cur=db.rawQuery("select * from "+tb_name,null);
        //app初始化
        //if(!gv.GetAllState().contains("default_1") && !gv.GetAllState().contains("default_2")){gv.init();}
        if(cur.getCount()==0){gv.init();}
        for(int k=0;k<gv.GetAllState().size(); k++){   //get keyboard
            keyboard.put(gv.GetAllState().get(k),keyboard_layout.get(k));
        }
        setKeyLabel();
        return keyboardView;

        // get the KeyboardView and add our Keyboard layout to it
        //KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        //Keyboard keyboard = new Keyboard(this, R.xml.number_pad);
        //keyboardView.setKeyboard(keyboard);
        //keyboardView.setOnKeyboardActionListener(this);
        //return keyboardView;
    }
    public void onStartInputView (EditorInfo info, boolean restarting){
        setInputView(onCreateInputView());
    }
    public void setKeyLabel(){  //設定按鍵符號
        //Keyboard keyboard_layout2 = keyboard.get(gv.GetAllState().get(currentStatePage)); //目前鍵盤的layout
        List<String> toolKey_array = new ArrayList<>(Arrays.asList(toolKey));

        ArrayList<Integer> nowHave =  new ArrayList<>();
        for(int n=0; n<gv.GetAllState().size(); n++){   //五鍵盤全部都設定label
            int tem=0;
            for(String s:gv.AllStateContext().get(gv.GetAllState().get(n))){
                if(s.equals("")){
                    tem++;
                }else {
                    nowHave.add(n);
                    break;
                }
            }
            if(tem==18||gv.AllStateContext().get(gv.GetAllState().get(n)).length==0){ continue; }

            int i=97;
            int k=97;
            for(Keyboard.Key key : keyboard.get(gv.GetAllState().get(n)).getKeys()){
                if(toolKey_array.contains( key.label.toString() )){
                    continue;
                }
                key.label=gv.AllStateContext().get(gv.GetAllState().get(n))[i-k];
                i++;
            }
        }
        //設置鍵盤
        if(nowHave.size()==0||nowHave==null){}
        else if(!nowHave.contains(currentStatePage)){
            keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(nowHave.get(0))) );
        }else{
            keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(currentStatePage)) );
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        gv = (GlobalVariable)getApplicationContext();

        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        if(primaryCode==65){   //Last page
            //切換為上一個state鍵盤
            while (true){
                currentStatePage--;     //往前移一頁
                if(currentStatePage<0){    //若目前為第一頁則跳到最後一頁
                    currentStatePage=gv.GetAllState().size()-1;
                }
                //keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(currentStatePage)) );    //設置前一頁鍵盤

                int tem=0;
                for(String s:gv.AllStateContext().get(gv.GetAllState().get(currentStatePage))){
                    if(s.equals("")){
                        tem++;
                    }else {
                        break;
                    }
                }
                if(tem!=18 && gv.AllStateContext().get(gv.GetAllState().get(currentStatePage)).length!=0){
                    keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(currentStatePage)) );
                    break;
                }
            }

        }
        else if(primaryCode==66){   //Last page
            //切換為下一個state鍵盤
            while (true){
                currentStatePage++;     //往後移一頁
                if(currentStatePage>gv.GetAllState().size()-1){    //若目前為最後一頁則跳到第一頁
                    currentStatePage=0;
                }
                //keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(currentStatePage)) );    //設置前一頁鍵盤

                int tem=0;
                for(String s:gv.AllStateContext().get(gv.GetAllState().get(currentStatePage))){
                    if(s.equals("")){
                        tem++;
                    }else {
                        break;
                    }
                }
                if(tem!=18 && gv.AllStateContext().get(gv.GetAllState().get(currentStatePage)).length!=0){
                    keyboardView.setKeyboard( keyboard.get(gv.GetAllState().get(currentStatePage)) );
                    break;
                }
            }

        }
        else if(primaryCode==Keyboard.KEYCODE_DELETE){
            CharSequence selectedText = ic.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                ic.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                ic.commitText("", 1);
            }
        }
        else if(primaryCode==32||primaryCode==10){
            char code = (char) primaryCode;
            ic.commitText(String.valueOf(code), 1);
            //ic.commitText(String.valueOf(keyCodes[0]), 1);
        }
        else{
            Keyboard keyboard_layout3 = keyboard.get(gv.GetAllState().get(currentStatePage));
            for(int i=0; i<keyboard_layout3.getKeys().size(); i++){
                if(keyboard_layout3.getKeys().get(i).codes[0]==primaryCode){
                    ic.commitText(keyboard_layout3.getKeys().get(i).label,1);
                }
            }
        }
        /*for(int i=97; i<=114; i++){
            for(Keyboard.Key key:k1.getKeys()){
                //if (key.label.equals("i")) {
                //    key.label = "ᙏ̤̫";
                //}
            }
        }*/

        /*switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            case 49:
                ic.commitText("ᙏ̤̫ ", 1);
                break;
            case 73:
                //List keyList = k1.getKeys();
                for(Keyboard.Key key:k1.getKeys()){

                    if (key.label.equals("i")) {
                        key.label = "ᙏ̤̫";
                    }
                }
                break;
            case 74:    //切換state
                if(currentState.equals(state[0])){
                    currentStateList=gv.curStateContext(state[1]);
                    currentState=state[1];
                    keyboardView.setKeyboard(k2);
                }
                else{
                    currentStateList=gv.curStateContext(state[0]);
                    currentState=state[0];
                    keyboardView.setKeyboard(k1);
                }
                break;
            default:
                char code = (char) primaryCode;
                ic.commitText(String.valueOf(code), 1);
                ic.commitText("ᙏ̤̫ ", 1);
                //ic.commitText(String.valueOf(keyCodes[0]), 1);
        }*/
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {

    }
}
