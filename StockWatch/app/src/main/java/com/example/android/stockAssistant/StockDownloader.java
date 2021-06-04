package com.example.android.stockAssistant;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockDownloader extends AsyncTask<String, Void, String> {

    private static final String TAG = "StockDownloader";
    private MainActivity mActivity;
    private static final String dataURL = " https://cloud.iexapis.com/stable/stock/";
    private static final String token = "/quote?token=pk_80b81b7883c346f1b3ec4a798f4db2b2";
    StockDownloader(MainActivity ma)
    {
        mActivity = ma;
    }

    @Override
    protected String doInBackground(String... strings) {

        String  endPoint = dataURL + strings[0] + token;
        Uri uri = Uri.parse(endPoint);
        String finalUrl = uri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "doInBackground: ", e);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        mActivity.addStockDetailsToDB(parseJson(s));
    }

    public StockDetails parseJson(String s) {
        StockDetails sd = new StockDetails();
        try {
            JSONObject jsonObject = new JSONObject(s);
            sd.setStockSymbol(jsonObject.getString("symbol"));
            sd.setCmpName(jsonObject.getString("companyName"));
            sd.setPrice(jsonObject.getDouble("latestPrice"));
            sd.setPriceChange(jsonObject.getDouble("change"));
            sd.setChangePercentage(jsonObject.getDouble("changePercent"));
            return sd;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }
}
