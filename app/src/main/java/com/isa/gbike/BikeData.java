package com.isa.gbike;

import android.util.Log;
import android.util.Xml;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;


/**
 * Created by Jeff Liaw on 2016/3/5.
 */
public class BikeData {

    private JsonObject FetchJsonObject(String sURL) {
        JsonObject rootobj = null;

        try {
            // Connect to the URL using java's native library
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            rootobj = root.getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootobj;
    }

    private JsonArray FetchStopInfo(JsonElement stoproot) {
        JsonArray result = new JsonArray();

        if(stoproot.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> stopset = stoproot.getAsJsonObject().entrySet();

            // Iterate the stop list
            for (Map.Entry<String, JsonElement> entry : stopset) {
                JsonObject stop = (JsonObject) entry.getValue();
                JsonObject mstop = new JsonObject();

                // Keep sna, sbi, lat, lng, bemp and act as JsonObject and store in result array
                mstop.add("sna", stop.get("sna"));
                mstop.add("sbi", stop.get("sbi"));
                mstop.add("lat", stop.get("lat"));
                mstop.add("lng", stop.get("lng"));
                mstop.add("bemp", stop.get("bemp"));
                mstop.add("act", stop.get("act"));
                mstop.add("ar", stop.get("ar"));
                result.add(mstop);
            }
        }
        else if(stoproot.isJsonArray()) {
            for (JsonElement entry : stoproot.getAsJsonArray()) {
                JsonObject stop = entry.getAsJsonObject();
                JsonObject mstop = new JsonObject();

                // Keep sna, sbi, lat, lng, bemp and act as JsonObject and store in result array
                mstop.add("sna", stop.get("sna"));
                mstop.add("sbi", stop.get("sbi"));
                mstop.add("lat", stop.get("lat"));
                mstop.add("lng", stop.get("lng"));
                mstop.add("bemp", stop.get("bemp"));
                mstop.add("act", stop.get("act"));
                mstop.add("ar", stop.get("ar"));
                result.add(mstop);
            }
        }
        else {
            Log.e("BikeData", "Neither JsonArray nor JsonObject.");
        }

        return result;
    }

    JsonArray FetchTaipei() {
        JsonObject rootobj = FetchJsonObject("https://tcgbusfs.blob.core.windows.net/blobyoubike/YouBikeTP.gz");
        JsonArray result = FetchStopInfo(rootobj.getAsJsonObject("retVal"));

        for(JsonElement obj : result) {
            System.out.println(obj.getAsJsonObject().get("sna"));
        }

        return result;
    }

    JsonArray FetchTaichung() {
        String sURL = "http://ybjson01.youbike.com.tw:1002/gwjs.json";
        JsonObject rootobj = FetchJsonObject(sURL);
        JsonArray result = FetchStopInfo(rootobj.getAsJsonObject("retVal"));

        for (JsonElement obj : result) {
            System.out.println(obj.getAsJsonObject().get("sna"));
        }

        return result;
    }

    JsonArray FetchTaoyuan() {
        JsonObject rootobj = FetchJsonObject("http://data.tycg.gov.tw/TYCG_OPD/api/v1/rest/datastore/a1b4714b-3b75-4ff8-a8f2-cc377e4eaa0f?format=json");
        JsonArray result = FetchStopInfo(rootobj.getAsJsonObject("result").getAsJsonArray("records"));

        for(JsonElement obj : result) {
            System.out.println(obj.getAsJsonObject().get("sna"));
        }

        return result;
    }

    void FetchKaoshiung() {
        XmlPullParser pullParser = Xml.newPullParser();
        try {
            URL text = new URL( "http://www.c-bike.com.tw/xml/stationlistopendata.aspx" );
            pullParser.setInput(text.openStream() , "utf-8");
            //eventType is used to observe the states of XML document
            int eventType = pullParser.getEventType();
            //parsing every tag before the end of XML document
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                //XmlPullParser.START_TAG means the starting tag ex:<id>
                if (eventType == XmlPullParser.START_TAG) {
                    String name = pullParser.getName();
                    System.out.println(name);
                }
                //XmlPullParser.TEXT is the content inside those two tags
                if (eventType==XmlPullParser.TEXT) {
                    String value = pullParser.getText();
                    System.out.println(value);
                }
                //continue to parse next tag
                try {
                    eventType = pullParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
