package com.example.han.realnoon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by CHAE on 2015-10-26.
 */
public class noonWidget extends AppWidgetProvider {
    private static RemoteViews updateViews;
    public static int themaValue=0;
    public static String t_Value="thema1";
    private static DisplayImageOptions displayOptions;
    public static String contentValue="content2";
    public static String ph="000-0000";
    public static boolean CLICK_FLAG = false;

    private static Item item;

    static {
        displayOptions = DisplayImageOptions.createSimple();
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        themaValue = 0;
        t_Value = "thema1";
        contentValue = "content2";
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        themaValue = 0;
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        themaValue = 0;

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        UILApplication.initImageLoader(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, widgetId);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("chae.widget.update")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thiswidget = new ComponentName(context, noonWidget.class);
            int[] ids = appWidgetManager.getAppWidgetIds(thiswidget);
            onUpdate(context, appWidgetManager, ids);
        }
        if (intent.getAction().equals("chae.widget.left")) {
            int value = intent.getIntExtra("T_value", 0);
            switch (value) {
                case 0:
                    t_Value = "thema4";
                    themaValue = 3;
                    break;
                case 1:
                    t_Value = "thema1";
                    themaValue = 0;
                    break;
                case 2:
                    t_Value="thema2";
                    themaValue = 1;
                    break;
                case 3:
                    t_Value = "thema3";
                    themaValue = 2;
                    break;
                default:
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thiswidget = new ComponentName(context, noonWidget.class);
            int[] ids = appWidgetManager.getAppWidgetIds(thiswidget);
            onUpdate(context, appWidgetManager, ids);
        }

        if (intent.getAction().equals("chae.widget.right")) {
            int value = intent.getIntExtra("T_value", 0);
            switch (value) {
                case 0:
                    t_Value = "thema2";
                    themaValue = 1;
                    break;
                case 1:
                    t_Value = "thema3";
                    themaValue = 2;
                    break;
                case 2:
                    t_Value = "thema4";
                    themaValue = 3;
                    break;
                case 3:
                    t_Value = "thema1";
                    themaValue = 0;
                    break;
                default:
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thiswidget = new ComponentName(context, noonWidget.class);
            int[] ids = appWidgetManager.getAppWidgetIds(thiswidget);
            onUpdate(context, appWidgetManager, ids);
        }

        if (intent.getAction().equals("chae.widget.click1")) {
            CLICK_FLAG = true;
            noonDb();
            Log.i("widget","click2 Clicked");
            Intent i = new Intent();
            i.setClassName("com.example.han.realnoon", "com.example.han.realnoon.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        if(intent.getAction().equals("chae.widget.click2")) {
            CLICK_FLAG = true;
            Log.i("widget", "click2 Clicked");
            Intent i = new Intent();
            i.setClassName("com.example.han.realnoon", "com.example.han.realnoon.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        if (intent.getAction().equals("chae.widget.swap")) {

        }
    }

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,final int appWidgetId) {


        String content = contentValue;
        int layoutId;
        if ("content1".equals(content)) {
            layoutId = R.layout.widget_layout;
            String thema = t_Value;
            updateViews = new RemoteViews(context.getPackageName(), layoutId);
            updateViews.setTextViewText(R.id.widget_tv, "음식집 추천");
            item = contentValue(MainActivity.ThemaItem, thema);
            ph = item.phone;
            String[] phn = ph.split("[-]");
            ph = "";
            for(int i=0; i<phn.length; i++) {
                ph += phn[i];
            }
            Log.i("widget", "before configure : " + ph);
            configureLayout(content, item);
            Intent left_intent = new Intent();
            Intent right_intent = new Intent();
            Intent click_intent = new Intent();
            Intent call_intent = new Intent();
            Intent swap_intent = new Intent();
            left_intent.putExtra("T_value", themaValue);
            right_intent.putExtra("T_value", themaValue);
            left_intent.setAction("chae.widget.left");
            right_intent.setAction("chae.widget.right");
            click_intent.setAction("chae.widget.click1");
            call_intent.setAction(Intent.ACTION_DIAL);
            call_intent.setData(Uri.parse("tel:" + ph));
            swap_intent.setAction("chae.widget.swap");
            PendingIntent pendingIntent_L = PendingIntent.getBroadcast(context, 0, left_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent_R = PendingIntent.getBroadcast(context, 0, right_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent_C = PendingIntent.getBroadcast(context, 0, click_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent_D = PendingIntent.getActivity(context, 0, call_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent_S = PendingIntent.getBroadcast(context, 0, swap_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.call_button, pendingIntent_D);
            updateViews.setOnClickPendingIntent(R.id.left_button, pendingIntent_L);
            updateViews.setOnClickPendingIntent(R.id.right_button, pendingIntent_R);
            updateViews.setOnClickPendingIntent(R.id.widget_click, pendingIntent_C);
            updateViews.setOnClickPendingIntent(R.id.widget_swap, pendingIntent_S);

            ImageSize minImazeSize = new ImageSize(120,400);
            ImageLoader.getInstance().loadImage(item.imageUrl, minImazeSize,displayOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    updateViews.setImageViewBitmap(R.id.widget_image,loadedImage);
                    appWidgetManager.updateAppWidget(appWidgetId, updateViews);
                }
            });
        } else if ("content2".equals(content)) {
            Log.i("widget","content2");
            layoutId = R.layout.widget_layout2;

            updateViews = new RemoteViews(context.getPackageName(), layoutId);

            updateViews.setTextViewText(R.id.widget_tv, "뉴스 추천");
            updateViews.setTextViewText(R.id.widget_title, GetNewsData.titlevec.get(0).toString());
            updateViews.setTextViewText(R.id.widget_sub, GetNewsData.descvec.get(0).toString().substring(0,30)+"...");


            Intent click_intent = new Intent();
            click_intent.setAction("chae.widget.click2");
            PendingIntent pendingIntent_C = PendingIntent.getBroadcast(context, 0, click_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.layout2, pendingIntent_C);

            String url = "http://222.116.135.76:8080/Noon/images/noon.png";
            ImageSize minImazeSize = new ImageSize(120,400);
            ImageLoader.getInstance().loadImage(url, minImazeSize, displayOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    updateViews.setImageViewBitmap(R.id.widget_image, loadedImage);
                    appWidgetManager.updateAppWidget(appWidgetId, updateViews);
                }
            });
        } else if ("content3".equals(content)) {
            //layoutId = R.layout.widget_layout2;
        } else if ("content4".equals(content)){
            //layoutId = R.layout.widget_layout2;
        } else {
            layoutId = R.layout.widget_layout;
        }


    }

    private static void configureLayout(String content, Item item1) {
        updateViews.setTextViewText(R.id.widget_title, item1.title);
        updateViews.setTextViewText(R.id.widget_cg, item1.category);
        updateViews.setTextViewText(R.id.widget_address, item1.address);
        Log.i("widget", "configure:" + item1.title + item1.imageUrl);

    }

    private static Item contentValue(ArrayList<Item> items, String string) {
        Item item1 = new Item();
        switch (string) {
            case "thema1":
                item1 = items.get(0);
                break;
            case "thema2":
                item1 = items.get(1);
                break;
            case "thema3":
                item1 = items.get(2);
                break;
            case "thema4":
                item1 = items.get(3);
                break;
            default:
        }
        return item1;
    }

    public void noonDb() {
        DBHandler dbHandler = DBHandler.open(MainActivity.mContext, item);
        dbHandler.click_time();
        //dbHandler.food_favorite_insert();
        dbHandler.close();
    }


}