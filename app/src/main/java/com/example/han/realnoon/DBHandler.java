package com.example.han.realnoon;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DBHandler {
    private NoonDatabase helper;
    private SQLiteDatabase db;
    public static Item item;
    private static int[] pattern = new int[7];
    private DBHandler(Context ctx) {
        this.helper = new NoonDatabase(ctx);
        this.db = helper.getWritableDatabase();
    }

    public static DBHandler open(Context ctx, Item tem) throws SQLException {
        DBHandler handler = new DBHandler(ctx);
        item = tem;
        return handler;
    }
    public static DBHandler open(Context ctx) throws SQLException {
        DBHandler handler = new DBHandler(ctx);
        return handler;
    }

    public void close() {
        helper.close();
    }

    public boolean insertAnni(Anni ani){
        ArrayList<Anni> Annis = new ArrayList<>();
        Cursor cursor= null;
        try{
            db.execSQL("INSERT INTO anni_profile (subject, year, month, day, cate) " +
                    "VALUES ('" + ani.getSubject() + "', " + ani.getYear() + ", " + ani.getMonth() + ", " + ani.getDay() + ",'"+ ani.getCate() +"');");

        }catch (Exception e){
            return false;
        }

        return true;
    }
    public boolean deleteAnni(Anni ani){
        ArrayList<Anni> Annis = new ArrayList<>();
        Cursor cursor= null;
        try{
            db.execSQL("DELETE FROM anni_profile WHERE _id="+ani.getSeq());

        }catch (Exception e){
            return false;
        }

        return true;
    }
    public ArrayList<Anni> selectAnniWithWhere(int month,int day){
        ArrayList<Anni> Annis = new ArrayList<>();
        Cursor cursor= null;
        cursor = db.rawQuery("select * from anni_profile;", null);
        Log.i("aaaa",""+cursor.getCount());
        if(cursor.getCount() <= 0)
            return null;
        int col1 = cursor.getColumnIndex("_id");
        int col2 = cursor.getColumnIndex("subject");
        int col3 = cursor.getColumnIndex("year");
        int col4 = cursor.getColumnIndex("month");
        int col5 = cursor.getColumnIndex("cate");
        while(cursor.moveToNext()){
            Anni item = new Anni();
            item.setSeq(cursor.getInt(col1));
            item.setSubject(cursor.getString(col2));
            item.setYear(cursor.getInt(col3));
            item.setMonth(cursor.getInt(col4));
            item.setCate(cursor.getString(col5));
            Annis.add(item);
        }
        return Annis;

    }



    public boolean updateAnni(Anni ani){
        ArrayList<Anni> Annis = new ArrayList<>();
        try{
            db.execSQL("UPDATE anni_profile SET " +
                    "subject='"+ani.getSubject()+"', cate='"+ani.getCate()+"', year="+ani.getYear()+", month="+ani.getMonth()+", day="+ani.getDay()+" WHERE _id="+ani.getSeq()+";");

        }catch (Exception e){
            return false;
        }

        return true;

    }

    public ArrayList<Anni> selectAnni(){
        ArrayList<Anni> Annis = new ArrayList<>();
        Cursor cursor= null;
        cursor = db.rawQuery("select * from anni_profile;", null);
        Log.i("aaaa",""+cursor.getCount());
        if(cursor.getCount() <= 0)
            return null;
        int col1 = cursor.getColumnIndex("_id");
        int col2 = cursor.getColumnIndex("subject");
        int col3 = cursor.getColumnIndex("year");
        int col4 = cursor.getColumnIndex("month");
        int col5 = cursor.getColumnIndex("cate");
        while(cursor.moveToNext()){
            Anni item = new Anni();
            item.setSeq(cursor.getInt(col1));
            item.setSubject(cursor.getString(col2));
            item.setYear(cursor.getInt(col3));
            item.setMonth(cursor.getInt(col4));
            item.setCate(cursor.getString(col5));
            Annis.add(item);
        }
        return Annis;

    }
    public String selectfood(String local) throws SQLException {
        Cursor cursor= null;
        ArrayList<Integer> weight = new ArrayList<>();
        ArrayList<String> foodname = new ArrayList<>();
        Log.i("aaaa","1"+staticMerge.food.size());
        String food = staticMerge.food.get(0);
        Log.i("aaaa","2"+"select * from food_favorite where local_name = '" + local + "' and food = '" + food + "';");
        for(int i=0; i<staticMerge.food.size();i++){
            food = staticMerge.food.get(i);
            cursor = db.rawQuery("select * from food_favorite where local_name = '" + local + "' and food = '" + food + "';", null);
            if (cursor.getCount() > 0 ) {
                cursor.moveToFirst();
            }else{
                cursor = db.rawQuery("select * from food_favorite where food = '" + food + "';", null);
                    if(cursor.getCount() > 0 ){
                        cursor.moveToFirst();
                    }else{
                        return "편의점";
                }
            }
            Log.i("aaaa","3"+cursor.getCount());
            Log.i("aaaa","4"+cursor.getPosition());
            int temp = cursor.getColumnIndex("weight");
            int temp2 = cursor.getColumnIndex("food");
            Log.i("aaaa","5"+temp);
            weight.add(i,cursor.getInt(temp));
            foodname.add(i, cursor.getString(temp2));

        }

        int temp = weight.get(0);
        int index = 0;
        for(int i=1; i < weight.size();i++){
            if(temp > weight.get(i)){

            }else {
                index = i;
                temp = weight.get(i);
            }
        }

        return foodname.get(index);
    }

    public void insert(String str) {
        try {
            db.execSQL(str);
            Log.i("db", "db insert succeessed");
        }catch(Exception e ) {
            Log.i("db", "db insert Failed.");
        }
    }

    public void widget_insert() {
        String local = item.address;
        String lc[] = local.split(" ");
        Calendar c = Calendar.getInstance();
        String today = String.valueOf(c.get(Calendar.YEAR));
        today += "-" + String.valueOf(c.get(Calendar.MONTH)+1);
        today += "-" + String.valueOf(c.get(Calendar.DATE));

        try {
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight, f_date) " +
                    "VALUES ('" + lc[2] + "', '" + item.title + "', '" + staticMerge.temp + "', '" + staticMerge.what + "', 1, '"+ today +"');");
            String update_sql = "UPDATE food_favorite AS t " +
                    "JOIN ( " +
                    "select local_name, food, wea, time, sum(weight) as sum_weight from food_favorite " +
                    "group by local_name, food, wea, time having count(*)>1) as j " +
                    "on t.food = j.food " +
                    "set t.weight = j.sum_weight;";
            //date 갱신까지.
            String delete_sql="delete p from food_favorite p " +
                    "join ( " +
                    "select max(_id) as max_id, local_name, food, wea, time " +
                    "from food_favorite pp group by local_name, food, wea, time " +
                    "having count(*)>1) as q " +
                    "on q.food = p.food " +
                    "where p._id < q.max_id;";
            Log.i("db", "db insert succeessed");
        }catch(Exception e) {
            Log.i("db", "widget_insert Failed.");
        }
    }

    public void food_pattern_insert() {
        try {
            db.execSQL("INSERT INTO food_pattern VALUES (null, " + pattern[0] + ", " + pattern[1] + ", " + pattern[2] + ", " + pattern[3] + ", " + pattern[4] + ", " + pattern[5] + ", " + pattern[6] + ");");
        }catch (Exception e) {
            Log.i("db","food_pattern_insert Failed");
        }
    }

    public void food_pattern_clean() {
        // 패턴 일주일에 한번씩 정리해주는 메서드

        Cursor cursor= null;
        String clean_sql1 = "UPDATE food_pattern as t " +
                "JOIN ( SELECT SUM(a) AS sa, SUM(b) AS sb, SUM(c) AS sc, SUM(d) AS sd, SUM(e) AS se, SUM(f) AS sf, SUM(g) AS sf " +
                "FROM food_pattern) as j " +
                "SET t.a=j.sa, t.b=j.sb, t.c=j.sc, t.d=j.sd, t.e=j.se, t.f=j.sf, t.g=j.sg;";
        String clean_sql2 = "DELETE FROM food_pattern WHERE _id not like 1;";
        String clean_sql3 = "SELECT a,b,c,d,e,f,g FROM food_pattern;";

        try {
            db.execSQL(clean_sql1);
            db.execSQL(clean_sql2);
        }catch(Exception e) {
            Log.i("db","food_pattern_clean Failed1");
        }
        cursor = db.rawQuery(clean_sql3, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        int maxP = 0;
        int P = 0;
        for(int i=0; i<cursor.getColumnCount(); i++) {
            P = cursor.getInt(i);
            pattern[i] = P;
            maxP = (maxP<P?P:maxP);
        }
        maxP = maxP-3;
        for(int i=0; i<cursor.getColumnCount();i++) {
            pattern[i] = (maxP<=pattern[i]?1:0);
        }
        String clean_sql4 = "UPDATE food_pattern " +
                "SET a="+pattern[0]+", b="+pattern[1]+", c="+pattern[2]+", d="+pattern[3]+", e="+pattern[4]+", f="+pattern[5]+", g="+pattern[6]+";";

        try {
            db.execSQL(clean_sql4);
        }catch(Exception e) {
            Log.i("db","food_patttern_clean Failed2");
        }

    }

    public void abode_insert() {
        String local = item.address;
        String lc[] = local.split(" ");

        try {
            db.execSQL("INSERT INTO abode VALUES (null, '" + lc[2] + "', '" + lc[3] + "', 1);");
        }catch (Exception e) {
            Log.i("db","food_pattern_insert Failed");
        }
    }
    public void abode_clean() {
        //거주지 하루에 한 번 클린해주는 메서드
        String clean_sql1 = "UPDATE abode AS t " +
                "JOIN ( " +
                "SELECT local_name, addr, SUM(weight) AS sum_weight FROM abode " +
                "GROUP BY local_name, addr HAVING count(*)>1) AS j " +
                "ON t.local_name = j.local_name " +
                "SET t.weight = j.sum_weight;";
        String clean_sql2 = "UPDATE abode AS t " +
                "JOIN ( " +
                "SELECT loacl_name, MAX(weight) AS max_weight FROM abode " +
                "GROUP BY weight HAVING count(*)>1) AS j " +
                "ON t.local_name = j.local_name " +
                "SET t.count = t.count+1;";
        // t.count == 0 인 거 삭제하고, 모든 weight 부분 0으로 update하면 끝.
        String clean_sql3 = "DELETE FROM adobe " +
                "WHERE count=0;";
        String clean_sql4 = "UPDATE abode " +
                "SET weight=0;";

        try{
            db.execSQL(clean_sql1);
            db.execSQL(clean_sql2);
            db.execSQL(clean_sql3);
            db.execSQL(clean_sql4);
        }catch(Exception e) {
            Log.i("db","abode_clean Failed");
        }
    }

    public void click_time() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        SharedInit si = new SharedInit(MainActivity.mContext);
        String timeValue = "";
        switch(staticMerge.what) {
            case "아침":
                timeValue="0";
                pattern[0]=1;
                break;
            case "아점":
                timeValue="1";
                pattern[1]=1;
                break;
            case "점심":
                timeValue="2";
                pattern[2]=1;
                break;
            case "점저":
                timeValue="3";
                pattern[3]=1;
                break;
            case "저녁":
                timeValue="4";
                pattern[4]=1;
                break;
            case "야식":
                timeValue="5";
                pattern[5]=1;
                break;
            case "후식":
                timeValue="6";
                pattern[6]=1;
                break;
            default:
        }

        Long tt = si.getSharedTime(timeValue);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String str = df.format(tt);
        String[] click_str = str.split(":");
        int h,m;
        h = Integer.valueOf(click_str[0]);
        m = Integer.valueOf(click_str[1]);

        hour = (h+hour)/2;
        min = (m+min)/2;
        if( (min-60)>=0 ) {
            hour += 1;
            min = min%60;
        }
        Log.i("widget","new hour : " + hour +",minute:"+min);
        //si.setSharedTime(timeValue, hour, min);
    }

}

