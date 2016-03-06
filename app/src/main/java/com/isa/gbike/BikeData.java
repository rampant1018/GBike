package com.isa.gbike;

import com.google.gson.*;

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
        Set<Map.Entry<String,JsonElement>> stopset = stoproot.getAsJsonObject().entrySet();
        JsonArray result = new JsonArray();

        // Iterate the stop list
        for(Map.Entry<String,JsonElement> entry : stopset) {
            JsonObject stop = (JsonObject)entry.getValue();
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

        return result;
    }

    JsonArray FetchTaichung() {
        String sURL = "http://ybjson01.youbike.com.tw:1002/gwjs.json";
        JsonObject rootobj = FetchJsonObject(sURL);
        JsonArray result = FetchStopInfo(rootobj.get("retVal"));

        for(JsonElement obj : result) {
            System.out.println(obj.getAsJsonObject().get("sna"));
        }

        return result;
    }
}
