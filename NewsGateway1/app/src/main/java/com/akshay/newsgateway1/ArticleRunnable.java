package com.akshay.newsgateway1;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleRunnable implements Runnable {

    private static final String TAG = "ArticleRunnable";
    private MainActivity mainActivity;
    private String newsChannelId;
    private NewsService newsService;
    private static final String apiKey = "1655208d48c84ea1bdadcf7f528a5233";
    private static String urlBeginning = "https://newsapi.org/v2/top-headlines?sources=";
    private static String urlEnd = "&language=en&apiKey=";

    ArticleRunnable(NewsService newsService, String newsChannelId) {
        this.newsService = newsService; this.newsChannelId = newsChannelId;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(urlBeginning + newsChannelId + urlEnd + apiKey);
        String urlToUse = dataUri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "");
            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                handleResults(null);
                Log.d(TAG, "run: Http NOt ok" );
                return;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append('\n');
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
        final ArrayList<Article> articleList = parseJSON(S);
        newsService.setArticles(articleList);
    }

    private ArrayList<Article> parseJSON(String s) {
        JSONArray jsonArrayObj=null;
        ArrayList<Article> articlesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);

            try {
                jsonArrayObj = jsonObject.getJSONArray("articles");
                for (int j = 0; j < jsonArrayObj.length(); j++) {
                    String articleAuthor = "";
                    String articleTitle = "";
                    String articleDescription = "";
                    String articleUrl="";
                    String articleUrltoimage="";
                    String articlePublishedAt="";
                    JSONObject singleArticleObj = null;
                    try {
                        singleArticleObj = (JSONObject) jsonArrayObj.get(j);
                    } catch (Exception e) {

                    }
                    try {
                        articleAuthor = singleArticleObj.getString("author");
                    } catch (Exception e) {

                    }
                    try {
                        articleTitle = singleArticleObj.getString("title");
                    } catch (Exception e) {

                    }
                    try {
                        articleDescription = singleArticleObj.getString("description");
                    } catch (Exception e) {

                    }
                    try {
                        articleUrl = singleArticleObj.getString("url");
                    } catch (Exception e) {

                    }
                    try {
                        articleUrltoimage = singleArticleObj.getString("urlToImage");
                    } catch (Exception e) {

                    }
                    try {
                        articlePublishedAt = singleArticleObj.getString("publishedAt");
                    } catch (Exception e) {

                    }
                    articlesList.add(new Article(articleAuthor,articleTitle,articleDescription,articleUrl,articleUrltoimage,articlePublishedAt));
                }
                return articlesList;
            } catch (Exception e) { }
        }
        catch (Exception e){

        }
        return null;
    }
}