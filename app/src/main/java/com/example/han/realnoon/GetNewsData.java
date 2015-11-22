package com.example.han.realnoon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

/**
 * Created by han on 2015-11-11.
 */
public class GetNewsData extends AsyncTask<Void,Void,Void> {


    NewsItem item;
    // �쎒�궗�씠�듃�뿉 �젒�냽�븷 二쇱냼
    String uri = "http://rss.hankyung.com/new/news_main.xml";
    // �쎒�궗�씠�듃�뿉 �젒�냽�쓣 �룄��二쇰뒗 �겢�옒�뒪
    URL url;
    // XML臾몄꽌�쓽 �궡�슜�쓣 �엫�떆濡� ���옣�븷 蹂��닔
    String tagname = "", title="", desc="", link ="", image = "";
    // �뜲�씠�꽣�쓽 �궡�슜�쓣 紐⑤몢 �씫�뼱�뱶�졇�뒗吏��뿉 ���븳 �젙蹂대�� ���옣
    Boolean flag = null;
    @Override
    protected void onPreExecute() {
        NewsItem item = new Item;
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Boolean flag = false;
        try {
            //xml臾몄꽌瑜� �씫怨� �빐�꽍�빐以� �닔 �엳�뒗 媛앹껜瑜� �꽑�뼵
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //�꽕�엫�뒪�럹�씠�뒪 �궗�슜�뿬遺�
            factory.setNamespaceAware(true);
            //�떎�젣 xml臾몄꽌瑜� �씫�뼱 �뱶由щ㈃�꽌 �뜲�씠�꽣瑜� 異붿텧�빐二쇰뒗 媛앹껜 �꽑�뼵
            XmlPullParser xpp = factory.newPullParser();

            // �쎒�궗�씠�듃�뿉 �젒�냽
            url = new URL(uri);
            // �궗�씠�듃 �젒�냽�썑�뿉 xml 臾몄꽌瑜� �씫�뼱�꽌 媛��졇�샂
            InputStream in = url.openStream();
            // �쎒�궗�씠�듃濡쒕��꽣 諛쏆븘�삩 xml臾몄꽌瑜� �씫�뼱�뱶由щ㈃�꽌 �뜲�씠�꽣瑜� 異붿텧�빐 二쇰뒗 XmlPullParser媛앹껜濡� �꽆寃⑥쨲
            xpp.setInput(in, "utf-8");

            // �씠踰ㅽ듃 �궡�슜�쓣 �궗�슜�븯湲� �쐞�빐�꽌 蹂��닔 �꽑�뼵
            int eventType = xpp.getEventType();
            // 諛섎났臾몄쓣 �궗�슜�븯�뿬 臾몄꽌�쓽 �걹源뚯� �씫�뼱 �뱾�씠硫댁꽌 �뜲�씠�꽣瑜� 異붿텧�븯�뿬 媛곴컖�쓽 踰≫꽣�뿉 ���옣
            while(eventType != XmlPullParser.END_DOCUMENT ) {
                if(eventType == XmlPullParser.START_TAG) {
                    // �깭洹몄쓽 �씠由꾩쓣 �븣�븘�빞 �뀓�뒪�듃瑜� ���옣�븯湲곗뿉 �깭洹몄씠由꾩쓣 �씫�뼱�꽌 蹂��닔�뿉 ���옣
                    if(xpp.getName().equals("item")){
                        flag = true;
                    }
                    tagname = xpp.getName();
                } else if(eventType == XmlPullParser.TEXT) {
                    if(flag){
                        if(tagname.equals("title")) title += xpp.getText();
                            // �깭洹� �씠由꾩씠 description怨� 媛숇떎硫� desc蹂��닔�뿉 ���옣
                        else if (tagname.equals("link")) link += xpp.getText();
                        else if (tagname.equals("description")) desc += xpp.getText();
                        else if (tagname.equals("image")) image += xpp.getText();
                    }
                    // �깭洹� �씠由꾩씠 title怨� 媛숇떎硫� 蹂��닔�뿉 title ���옣
                } else if (eventType == XmlPullParser.END_TAG) {
                    // end tag �씠由꾩쓣 �뼸�뼱�샂
                    tagname = xpp.getName();
                    // end tag �씠由꾩씠 item�씠�씪硫� ���옣�븳 蹂��닔 title怨� desc瑜� 踰≫꽣�뿉 ���옣
                    if(tagname.equals("item")) {
                       
                        item.setTitle(title);
                        item.setDesc(desc);
                        item.setImageUrl(image);
                        item.setLink(link);
                        
                        NewsNews.add(item);
                        
                        // 蹂��닔 珥덇린�솕
                        title="";
                        desc="";
                        link="";
                        image="";
                    }
                }
                // �떎�쓬 �씠踰ㅽ듃瑜� �꽆源�
                eventType = xpp.next();
            }
            // 紐⑤뱺 xml臾몄꽌瑜� �씫�뼱�뱶�졇�떎硫�
            flag = true;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity.mHandler.sendEmptyMessage(0);

        super.onPostExecute(aVoid);
    }

}
