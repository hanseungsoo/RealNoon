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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHAE on 2015-09-19.
 */

public class foodDbJson extends AsyncTask<String,Void,Void> {
    @Override
    protected void onPostExecute(Void aVoid) {

        DBHandler dh = DBHandler.open(MainActivity.mContext);
        staticMerge.finish_food = dh.selectfood(staticMerge.dong);
        if(staticMerge.finish_food.equals("")){
            staticMerge.finish_food = "편의점";
        }
        try{
            LocationManager locationManager = (LocationManager) MainActivity.mContext.getSystemService(Context.LOCATION_SERVICE);
            int radius = 1000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
            int page = 1;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Searcher searcher1 = new Searcher();
            searcher1.searchKeyword(MainActivity.mContext, "편의점", latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {

                    MainActivity.ThemaItem.add(0, itemList.get(0));

                }

                @Override
                public void onFail() {
                }
            });
            Searcher searcher2 = new Searcher();
            searcher2.searchKeyword(MainActivity.mContext, staticMerge.finish_food, latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {
                    MainActivity.ThemaItem.add(1, itemList.get(0));
                }

                @Override
                public void onFail() {
                }
            });
            Searcher searcher3 = new Searcher();
            searcher3.searchCategory(MainActivity.mContext, "FD6", latitude, longitude, radius, page, 2, "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {
                    MainActivity.ThemaItem.add(2, itemList.get(0));
                }

                @Override
                public void onFail() {
                }
            });
            Searcher searcher4 = new Searcher();
            searcher4.searchCategory(MainActivity.mContext, "FD6", latitude, longitude, radius, 1, (int) (Math.random() * 3), "9db6272582177f1d7b0643e35e1993e9", new OnFinishSearchListener() {
                @Override
                public void onSuccess(List<Item> itemList) {
                    MainActivity.ThemaItem.add(3, itemList.get((int) (Math.random() * 15)));
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
        staticMerge.food = foodDB;
        return null;
    }

}