package com.example.han.realnoon;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by CHAE on 2015-11-09.
 */
public class DBActivity extends Activity {
    DBHandler dbHandler;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        dbHandler = DBHandler.open(MainActivity.mContext);
        tv = (TextView)findViewById(R.id.dbtv);
    }
    @Override
    public void onStop() {
        super.onStop();
        dbHandler.close();
    }

    public void Button1CLick(View v) {
        //음식패턴 보기
        Cursor cursor = dbHandler.select_food_pattern();
        String str = "";
        String str1 = "패턴1:";
        for(int j =0; j<cursor.getCount(); j++) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                str += str1 + String.valueOf(cursor.getInt(i));
                str1=", ";
            }
            str1="패턴"+(j+2)+":";
            cursor.moveToNext();
            str += "\n";
        }
        tv.setText(str);
    }
    public void Button21CLick(View v) {
        //음식패턴 정리
        dbHandler.food_pattern_clean1();
        //dbHandler.food_pattern_clean2();
    }
    public void Button22CLick(View v) {
        //음식패턴 정리
        dbHandler.food_pattern_clean2();
    }
    public void Button3CLick(View v) {
        //거주지 보기
        Cursor cursor = dbHandler.select_abode();
        String str = "";
        String str1 = "거주지1:";
        for(int j =0; j<cursor.getCount(); j++) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                str += str1 + cursor.getString(i);
                str1=", ";
            }
            str1="거주지"+(j+2)+":";
            cursor.moveToNext();
            str += "\n";
        }
        tv.setText(str);
    }
    public void Button4CLick(View v) {
        //거주지 정리
        dbHandler.abode_clean();
    }
}
