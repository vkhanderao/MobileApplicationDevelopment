package com.akshay.newsgateway1;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class NewsService extends Service {

    private static final String TAG = "NewsService";
    private boolean running = true;
    private final ArrayList<Article> articleList = new ArrayList<>();
    private ServiceReceiver serviceReceiver;
    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(MainActivity.ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (running)
                {
                    while(articleList.isEmpty())
                    {
                        try
                        {
                            Thread.sleep(250);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent();
                    intent.setAction(MainActivity.ACTION_NEWS_STORY);
                    intent.putExtra(MainActivity.ARTICLE_LIST, articleList);
                    sendBroadcast(intent);
                    articleList.clear();
                }
            }
        }).start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        running = false;
        unregisterReceiver(serviceReceiver);
        super.onDestroy();
    }

    public void setArticles(ArrayList<Article> articles)
    {
        articleList.clear();
        articleList.addAll(articles);
    }

    class ServiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction())
            {
                case MainActivity.ACTION_MSG_TO_SERVICE:
                    Sources sourceObj = null;
                    if (intent.hasExtra(MainActivity.SOURCE))
                    {
                        sourceObj = (Sources) intent.getSerializableExtra(MainActivity.SOURCE);
                    }
                    assert sourceObj != null;
                    ArticleRunnable articleRunnable = new ArticleRunnable(NewsService.this,sourceObj.getId());
                    new Thread(articleRunnable).start();
                    break;

            }
        }
    }



}

