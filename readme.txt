遇到問題與解決
1. sql語法中除數字外(eg.文字)要用單引號(')框起來
   舉例: (GlobalVariable.java)
	db.update(tb_name,cv,"state='"+editState+"'",null);
2. sql中ContentValues cv = new ContentValues(  你的數量【eg.欄位數】  );
    cv.put(欄位名稱, 資料內容)...
    舉例: (GlobalVariable.java)
            ContentValues cv = new ContentValues(2);
            cv.put("state", editState);
            cv.put("content", pre_default);
            db.update(tb_name,cv,"state='"+editState+"'",null);

問題無解
(MainActivity.java  第183行~196行)
1. 多出時無法實現刪除(已知要改
   a.  txtContent.remove(-1)  改成   txtContent.remove(txtContent.size()-1)
   b.  txtContent.remove("");  改成  //txtContent.remove("");
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