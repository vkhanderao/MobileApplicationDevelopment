package com.akshay.newsgateway1;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SourcesRunnable implements Runnable {

    private static final String TAG = "SourcesRunnable";
    private MainActivity mainActivity;
    private static final String apiKey = "1655208d48c84ea1bdadcf7f528a5233";
    private static String urlStart = "https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=";

    SourcesRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(urlStart + apiKey);
        String urlToUse = dataUri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "");
            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: Http NOt ok" );
                handleResults(null);
                return;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    private void handleResults(String S){
        if(S==null){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.showToast();
                }
            });
        }
        final HashMap<String, ArrayList<Sources>> sourceMapList = parseJSON(S);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.setupCategory(sourceMapList);
            }
        });
    }

    private HashMap<String, ArrayList<Sources>> parseJSON(String s) {

        JSONArray jsonArrayObj=null;

        ArrayList<Sources> sourceList = new ArrayList<>();
        Sources srcObj;
        HashMap<String, ArrayList<Sources>> sourceMap = new HashMap<>();
        ArrayList<Sources> tempList;
        try {
            JSONObject jsonObject = new JSONObject(s);

            try {
                jsonArrayObj = jsonObject.getJSONArray("sources");
                for (int j = 0; j < jsonArrayObj.length(); j++) {
                    String sourceId = "";
                    String sourceName = "";
                    String sourceCat = "";
                    JSONObject singleSourceObj = null;
                    srcObj = new Sources();
                    try {
                        singleSourceObj = (JSONObject) jsonArrayObj.get(j);
                    } catch (Exception e) {
                    }
                    try {
                        sourceId = singleSourceObj.getString("id");
                        srcObj.setId(sourceId);
                    } catch (Exception e) {
                    }
                    try {
                        sourceName = singleSourceObj.getString("name");
                        srcObj.setName(sourceName);
                    } catch (Exception e) {
                    }
                    try {
                        sourceCat = singleSourceObj.getString("category");
                        srcObj.setCategory(sourceCat);
                    } catch (Exception e) {
                    }
                    sourceList.add(srcObj);
                    if (!sourceMap.containsKey(srcObj.getCategory().toLowerCase())) {
                        sourceMap.put(srcObj.getCategory(), new ArrayList<Sources>());
                    }
                    tempList = sourceMap.get(srcObj.getCategory());
                    tempList.add(srcObj);
                    sourceMap.put(srcObj.getCategory(),tempList);
                }
                sourceMap.put("all", sourceList);
                return sourceMap;
            } catch (Exception e) { }
        }
        catch (Exception e){
        }
        return null;
    }
}


