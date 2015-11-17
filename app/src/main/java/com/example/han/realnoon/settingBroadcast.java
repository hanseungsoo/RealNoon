package com.example.han.realnoon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by han on 2015-11-09.
 */
public class settingBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("ACTION.SET.PLACE")){
            // 30분
            DBHandler dbHandler = DBHandler.open(MainActivity.mContext);
            dbHandler.abode_insert();
            dbHandler.close();

        }
        if(intent.getAction().equals("ACTION.SET.PATTEN")){
            // 1일
            DBHandler dbHandler = DBHandler.open(MainActivity.mContext);
            dbHandler.food_pattern_insert();
            dbHandler.abode_clean();
            dbHandler.close();

        }
        if(intent.getAction().equals("ACTION.SET.ONEWEEK")){
            // 1주
        }
        if(intent.getAction().equals("ACTION.SET.NEWS")){
            GetNewsData GND = new GetNewsData();
            GND.execute();
        }
    }
}
