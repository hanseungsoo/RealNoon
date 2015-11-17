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
        db.close();
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
    public void food_favorite_insert() {
        String local = item.address;
        String lc[] = local.split(" ");
        Calendar c = Calendar.getInstance();
        String today = String.valueOf(c.get(Calendar.YEAR));
        today += "-" + String.valueOf(c.get(Calendar.MONTH)+1);
        today += "-" + String.valueOf(c.get(Calendar.DATE));

        try {
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight, f_date) " +
                    "VALUES ('" + lc[2] + "', '" + item.title + "', '" + staticMerge.temp + "', '" + staticMerge.what + "', 1, '" + today + "');");
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
            Log.i("db", "food_pattern_insert called");
            db.execSQL("INSERT INTO food_pattern VALUES (null, " + pattern[0] + ", " + pattern[1] + ", " + pattern[2] + ", " + pattern[3] + ", " + pattern[4] + ", " + pattern[5] + ", " + pattern[6] + ");");
        }catch (Exception e) {
            Log.i("db","food_pattern_insert Failed");
        }
    }

    public void food_pattern_clean1() {
        // 패턴 일주일에 한번씩 정리해주는 메서드

        Cursor cursor= null;
        String clean_sql1 = "SELECT SUM(a),SUM(b),SUM(c),SUM(d),SUM(e),SUM(f),SUM(g) FROM food_pattern;";
        //String clean_sql1 = "REPLACE INTO food_pattern (a, b) SELECT SUM(a), SUM(b) FROM food_pattern";
        String clean_sql2 = "DELETE FROM food_pattern;";
        String clean_sql3 = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'food_pattern';";


        cursor = db.rawQuery(clean_sql1, null);
        if(cursor.getCount() >0) {
            cursor.moveToFirst();
            int maxP = 0;
            int P = 0;
            for(int i=0; i<cursor.getColumnCount(); i++) {
                P = cursor.getInt(i);
                pattern[i] = P;
                Log.i("db",""+pattern[i]);
                maxP = (maxP<P?P:maxP);
            }
            maxP = maxP-3;
            for(int i=0; i<cursor.getColumnCount();i++) {
                pattern[i] = (maxP<=pattern[i]?1:0);

            }
        }

        String clean_sql4 = "INSERT INTO food_pattern (a,b,c,d,e,f,g) VALUES " +
                "("+pattern[0]+","+pattern[1]+","+pattern[2]+","+pattern[3]+","+pattern[4]+","+pattern[5]+","+pattern[6]+");";
        try {
            db.execSQL(clean_sql2);
            db.execSQL(clean_sql3);
            db.execSQL(clean_sql4);
        }catch(Exception e) {
            Log.i("db","food_pattern_clean Failed1");
        }
    }
    public void food_pattern_clean2() {
        // 패턴 일주일에 한번씩 정리해주는 메서드

        Cursor cursor= null;
        String clean_sql1 = "SELECT SUM(a),SUM(b),SUM(c),SUM(d),SUM(e),SUM(f),SUM(g) FROM food_pattern;";


        cursor = db.rawQuery(clean_sql1, null);
        if(cursor.getCount() >0) {
            cursor.moveToFirst();
            int maxP = 0;
            int minP = 0;
            int P = 0;
            for(int i=0; i<cursor.getColumnCount(); i++) {
                P = cursor.getInt(i);
                pattern[i] = P;
                Log.i("db",""+pattern[i]);
                maxP = (maxP<P?P:maxP);
                minP = (P>minP?minP:P);
            }
            int avgP = (maxP+minP)/2;

            SharedInit si = new SharedInit(MainActivity.mContext);

            for(int i=0; i<cursor.getColumnCount();i++) {
                pattern[i] = (avgP<=pattern[i]?1:0);
                si.setSharedTrue(String.valueOf(i),(pattern[i]>0?true:false));
            }
        }
    }
    public void abode_insert() {
        String local = item.address;
        String lc[] = local.split(" ");
        if(!item.phone.equals("010-2043-5392")) {
            try {
                db.execSQL("INSERT INTO abode VALUES (null, '" + lc[2] + "', '" + lc[3] + "', 1);");
            }catch (Exception e) {
                Log.i("db","abode_insert Failed");
            }
        }
    }
    public void abode_clean() {
        //거주지 하루에 한 번 클린해주는 메서드
        Cursor cursor= null;
        int maxP=0;
        int weight = 0;
        int id = 0;
        String loc1 = "";
        String loc2 = "";
        int count = 0;
        String clean_sql1 = "SELECT _id, COUNT(_id), local_name, addr, count FROM abode GROUP BY local_name, addr HAVING COUNT(*)>1;";
        String clean_sql2 = "";
        String clean_sql3 = "";
        String clean_sql4 = "";
        String clean_sql5 = "";

        cursor = db.rawQuery(clean_sql1, null);
        if(cursor.getCount() >0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                id = cursor.getInt(0);
                weight = cursor.getInt(1);
                loc1 = cursor.getString(2);
                loc2 = cursor.getString(3);
                count = cursor.getInt(4);
                maxP = (maxP > weight ? maxP : weight);

                clean_sql2 = "UPDATE abode SET weight=" + weight + " WHERE local_name='" + loc1 + "' AND addr='" + loc2 + "';";
            }
        }
        clean_sql3 = "DELETE FROM abode " +
                "WHERE count NOT IN " +
                "(SELECT MAX(count) FROM abode GROUP BY addr, weight);";
        clean_sql4 = "DELETE FROM abode " +
                "WHERE EXISTS (SELECT _id FROM abode WHERE count=0 " +
                "AND EXISTS (SELECT _id FROM abode WHERE count>0));";
        clean_sql5 = "UPDATE abode SET count = (count+1), weight=0;";
        String clean_sql6 = "DELETE FROM abode WHERE _id NOT IN " +
                "(SELECT * FROM (SELECT MIN(_id) FROM abode GROUP BY local_name, addr) AS T);";
        try {
            db.execSQL(clean_sql2);
            db.execSQL(clean_sql3);
            db.execSQL(clean_sql4);
            db.execSQL(clean_sql5);
            db.execSQL(clean_sql6);
        }catch (Exception e) {
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

    public void dummy_insert() {
        try {
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight) VALUES ('단월동','파전','rain','아침',1);");
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight) VALUES ('단월동','피자','broken clouds','아침',1);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,1,1,1,1,1,1);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,0,0,1,0,0,0);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 0,1,1,1,0,0,0);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,0,0,1,0,0,1);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 0,0,1,1,1,1,1);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,0,0,1,0,0,0);");
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,0,0,1,0,0,0);");
            db.execSQL("INSERT INTO abode VALUES (null, '단월동','458-101', 1, 0);");
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight, f_date) " +
                    "VALUES ('단월동','파전','rain', null, 1, '2015-11-10');");
        }catch (Exception e) {
            Log.i("db","dummy_insert Failed");
        }
    }

    public Cursor select_food_pattern() {
        Cursor cursor = null;
        // cursor = db.rawQuery("select * from food_favorite where local_name = ? AND local_name = ?", new String[] {"David","2"});
        cursor = db.rawQuery("SELECT a,b,c,d,e,f,g FROM food_pattern",null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() >0) {
            cursor.moveToFirst();
            return cursor;
        }

        return null;
    }

    public Cursor select_abode() {
        Cursor cursor =null;
        cursor = db.rawQuery("SELECT * from abode",null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() >0) {
            cursor.moveToFirst();
            return cursor;
        }

        return null;
    }

}

