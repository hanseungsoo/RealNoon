package com.example.han.realnoon;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CHAE on 2015-09-19.
 */

public class foodDbJson extends AsyncTask<String,Void,Void> {
    @Override
    protected void onPostExecute(Void aVoid) {

        DBHandler dh = DBHandler.open(MainActivity.mContext);
        staticMerge.finish_food[0] = dh.selectfood(staticMerge.dong);
        if(staticMerge.finish_food[0].equals("")){
            staticMerge.finish_food[0] = "편의점";
        }

        try{
            LocationManager locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);
            int radius = 1000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
            int page = 1;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if(!(staticMerge.finish_food[1].equals("empty"))){
                Searcher searcher1 = new Searcher();
                searcher1.searchKeyword(MainActivity.mContext, staticMerge.finish_food[1], latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
                        if(itemList.size() == 0){
                            Item item = new Item();
                            item.title = "주변에";
                            item.category = "음식점이 없습니다.";
                            item.imageUrl = "http://222.116.135.76:8080/Noon/images/noon.png";
                            item.address = "(X)address";
                            item.phone = "추천할만한";
                            MainActivity.ThemaItem.set( 0 , item);
                            MainActivity.mHandler.sendEmptyMessage(1);
                        }else{
                            MainActivity.ThemaItem.set(0,itemList.get(0));
                        }
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }

            Searcher searcher2 = new Searcher();
            searcher2.searchKeyword(MainActivity.mContext, staticMerge.finish_food[0], latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {
                    if(itemList.size() == 0){
                        Item item = new Item();
                        item.title = "주변에";
                        item.imageUrl = "http://222.116.135.76:8080/Noon/images/noon.png";
                        item.category = "음식점이 없습니다.";
                        item.address = "(X)address";
                        item.phone = "추천할만한";
                        MainActivity.ThemaItem.set( 1 , item);
                    }else{
                        MainActivity.ThemaItem.set(1, itemList.get(0));
                    }
                }

                @Override
                public void onFail() {
                }
            });
            Searcher searcher3 = new Searcher();
            searcher3.searchCategory(MainActivity.mContext, "FD6", latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {
                    if(itemList.size() == 0){
                        Item item = new Item();
                        item.title = "주변에";
                        item.category = "음식점이 없습니다.";
                        item.imageUrl = "http://222.116.135.76:8080/Noon/images/noon.png";
                        item.address = "(X)address";
                        item.phone = "추천할만한";
                        MainActivity.ThemaItem.set( 2 , item);
                    }else{
                        MainActivity.ThemaItem.set(2, itemList.get(0));
                    }
                }

                @Override
                public void onFail() {
                }
            });
            Searcher searcher4 = new Searcher();
            searcher4.searchCategory(MainActivity.mContext, "FD6", latitude, longitude, radius, 1, (int) (Math.random() * 3), "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {

                    if (itemList.size() == 0) {
                        Item item = new Item();
                        item.title = "주변에";
                        item.category = "음식점이 없습니다.";
                        item.address = "(X)address";
                        item.imageUrl = "http://222.116.135.76:8080/Noon/images/noon.png";
                        item.phone = "추천할만한";
                        MainActivity.ThemaItem.set(3, item);
                    } else {
                        MainActivity.ThemaItem.set(3, itemList.get((int) (Math.random() * 15)));
                    }
                }

                @Override
                public void onFail() {
                }
            });
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("aaaa", exceptionAsStrting);
        }


        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {
        String myResult = "";
        ArrayList<String> foodDB = new ArrayList<String>();

        try {
            Log.i("aaaa", "-----출출1" + params[0]);
            URL url = new URL("http://222.116.135.76:8080/Noon/createJson.jsp?" + params[0]);
            URLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            http.setChunkedStreamingMode(0);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
            //InputStream in = http.getInputStream();

            StringBuffer sb = new StringBuffer();
            try {
                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }
                myResult = sb.toString();
                Log.i("aaaa", myResult);
            } finally {
                in.close();
            }
            try {
                JSONObject root = new JSONObject(myResult);
                JSONArray jarr = root.getJSONArray("Food");
                String food_name = "";
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = new JSONObject();
                    json = jarr.getJSONObject(i);
                    food_name = json.getString("food_name");
                    foodDB.add(food_name);
                    Log.i("aaaa", food_name);
                }

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("aaaa", exceptionAsStrting);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("aaaa", exceptionAsStrting);
        }

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        int month,day;
        month = Integer.parseInt(CurMonthFormat.format(date));
        day = Integer.parseInt(CurDayFormat.format(date));
        DBHandler dh = DBHandler.open(MainActivity.mContext);
        ArrayList<Anni> Annis = dh.selectAnniWithWhere(month, day);
        String food_nameAnniv = "";
        if(Annis == null){
            try {
                Log.i("aaaa", "-----출출2" + params[1]);
                URL url = new URL("http://222.116.135.76:8080/Noon/createJson.jsp?" + params[1]);
                URLConnection conn = (HttpURLConnection) url.openConnection();
                HttpURLConnection http = (HttpURLConnection) conn;

                http.setChunkedStreamingMode(0);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestMethod("POST");

                BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
                //InputStream in = http.getInputStream();
                StringBuffer sb = new StringBuffer();
                try {
                    int chr;
                    while ((chr = in.read()) != -1) {
                        sb.append((char) chr);
                    }
                    myResult = sb.toString();
                    Log.i("aaaa", myResult);
                } finally {
                    in.close();
                }
                try {
                    JSONObject root = new JSONObject(myResult);
                    JSONArray jarr = root.getJSONArray("Food");
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject json = new JSONObject();
                        json = jarr.getJSONObject(i);
                        food_nameAnniv = json.getString("food_name");
                        Log.i("aaaa", food_nameAnniv);
                    }

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsStrting = sw.toString();
                    Log.e("aaaa", exceptionAsStrting);
                }

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("aaaa", exceptionAsStrting);
            }
            if(food_nameAnniv.equals("No")){
                try {
                    Log.i("aaaa", "-----출출3" + params[2]);
                    URL url = new URL("http://222.116.135.76:8080/Noon/createJson.jsp?" + params[2]);
                    URLConnection conn = (HttpURLConnection) url.openConnection();
                    HttpURLConnection http = (HttpURLConnection) conn;

                    http.setChunkedStreamingMode(0);
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");

                    BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
                    //InputStream in = http.getInputStream();

                    StringBuffer sb = new StringBuffer();
                    try {
                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        myResult = sb.toString();
                        Log.i("aaaa", myResult);
                    } finally {
                        in.close();
                    }
                    try {
                        JSONObject root = new JSONObject(myResult);
                        JSONArray jarr = root.getJSONArray("Food");
                        food_nameAnniv = "";
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject json = new JSONObject();
                            json = jarr.getJSONObject(i);
                            food_nameAnniv = json.getString("food_name");
                            Log.i("aaaa", food_nameAnniv);
                        }

                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsStrting = sw.toString();
                        Log.e("aaaa", exceptionAsStrting);
                    }

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsStrting = sw.toString();
                    Log.e("aaaa", exceptionAsStrting);
                }
                if(food_nameAnniv.equals("No")){
                    Item item = new Item();
                    item.title = "오늘은";
                    item.category = "없습니다";
                    item.imageUrl = "http://222.116.135.76:8080/Noon/images/noon.png";
                    item.address = "(X)address";
                    item.phone = "기념일이";
                    MainActivity.ThemaItem.add( 0, item);
                    staticMerge.finish_food[1]="empty";
                }else{
                    staticMerge.finish_food[1]=food_nameAnniv;
                }
            }else{
                staticMerge.finish_food[1]=food_nameAnniv;
            }
        }else{
            staticMerge.finish_food[1] = Annis.get(0).getSubject();
        }


        staticMerge.food = foodDB;
        return null;
    }

}