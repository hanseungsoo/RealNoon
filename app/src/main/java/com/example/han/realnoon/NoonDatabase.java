package com.example.han.realnoon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CHAE on 2015-09-17.
 */
public class NoonDatabase extends SQLiteOpenHelper {


    public NoonDatabase (Context context) {
        super(context, "Noon.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SQL1 = "CREATE TABLE IF NOT EXISTS food_pattern " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "a INTENGER, b INTEGER, c INTEGER, d INTEGER, e INTEGER, f INTEGER, g INTEGER);";;
        String CREATE_SQL2 = "CREATE TABLE IF NOT EXISTS abode " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "local_name TEXT, addr TEXT, count INTEGER);";
        String CREATE_SQL3 = "CREATE TABLE IF NOT EXISTS food_favorite " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "local_name TEXT, food TEXT, wea TEXT, time TEXT, weight INTEGER, f_date TEXT);";
        String CREATE_SQL4 = "CREATE TABLE IF NOT EXISTS anni_profile" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "subject TEXT, year INTEGER, month INTEGER,day INTEGER, cate TEXT);";

        try {
            db.execSQL(CREATE_SQL1);
            db.execSQL(CREATE_SQL2);
            db.execSQL(CREATE_SQL3);
            db.execSQL(CREATE_SQL4);
            Log.i("widgeta","create sql");
        } catch(Exception ex) {
            Log.i("widgeta", "Exception in CREATE_SQL");
        }

        try {
            db.execSQL("INSERT INTO food_pattern VALUES (null, 1,1,1,1,1,1,1);");
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight) VALUES ('단월동','파전','rain','아침',1);");
            db.execSQL("INSERT INTO food_favorite (local_name, food, wea, time, weight) VALUES ('단월동','피자','broken clouds','아침',1);");

            Log.i("widget","insert sql");
        } catch(Exception ex) {
            Log.i("widgeta", "Exception in INSERT_SQL");
        }

    } // onCreate 끝

    public void onOpen(SQLiteDatabase db) {

    }// onOpen 끝

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("widget", "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        if(newVersion >1) {
            db.execSQL("DROP TABLE IF EXISTS food_pattern");
            db.execSQL("DROP TABLE IF EXISTS abode");
            db.execSQL("DROP TABLE IF EXISTS food_favorite");
            onCreate(db);
        }
    }//onUpgrade 끝끝

}