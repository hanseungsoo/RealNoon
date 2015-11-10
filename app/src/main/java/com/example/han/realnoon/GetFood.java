package com.example.han.realnoon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by han on 2015-09-14.
 */
public class GetFood extends BroadcastReceiver {

    Context context;



    public void getItem(Intent intent,String index,String what){

        String query ="";
        try{
            query = URLEncoder.encode("아침", "utf-8");
        }catch (Exception e){}
        Log.i("aaaa","main onCreate : foodDbJson");
        //String param = "tbName=noon_food&col=food_name&food_wea="+GetFood+"&food_time="+query;
        String param = "tbName=noon_food&col=food_name&food_wea=rain&food_time="+query;
        foodDbJson fD = new foodDbJson();
        fD.execute(param);

        try {
            Log.i("aaaa", "-----------------------------" + staticMerge.temp);


        } catch (Exception e) {

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("aaaa", exceptionAsStrting);
        }
        registerAlarm rA = new registerAlarm(context);
        rA.registerAM(intent.getAction(),index);
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        if(intent.getAction().equals("ACTION.GET.ONE")){
            staticMerge.what = "아침";
            getItem(intent,"0",staticMerge.what);
        }
        if(intent.getAction().equals("ACTION.GET.TWO")){
            staticMerge.what = "아점";
            getItem(intent,"1",staticMerge.what);
        }
        if(intent.getAction().equals("ACTION.GET.THREE")){
            staticMerge.what = "점심";
            getItem(intent,"2",staticMerge.what);

        }
        if(intent.getAction().equals("ACTION.GET.FOUR")){
            staticMerge.what = "점저";
            getItem(intent,"3",staticMerge.what);
        }
        if(intent.getAction().equals("ACTION.GET.FIVE")){
            staticMerge.what = "저녁";
            getItem(intent,"4",staticMerge.what);

        }
        if(intent.getAction().equals("ACTION.GET.SIX")){
            staticMerge.what="야식";
            getItem(intent,"5",staticMerge.what);
        }
        if(intent.getAction().equals("ACTION.GET.SEVEN")){
            staticMerge.what = "후식";
            getItem(intent,"6",staticMerge.what);

        }
        if(intent.getAction().substring(0, 10).equals("ACTION.GET"))
        {
            MainActivity.mmmm();
            Log.i("widget","receive:action.get ---> widget update");
        }

    }
}
