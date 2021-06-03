package com.akshay.newsgateway1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String ARTICLE_LIST = "ARTICLE_LIST";
    static final String SOURCE = "SOURCE";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ViewPager pager;
    Menu menu;
    List<Fragment> fragments;
    FragmentAdapter pageAdapter;
    NewsReceiver newsReceiver;
    private HashMap<String, ArrayList<Sources>> sourcesData = new HashMap<>();
    ArrayList<Sources> sourceArrayList;
    ArrayList<Article> articleArrayList;
    private int position=0;
    private String Name;
    private String category;
    private String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sourceArrayList = new ArrayList<>();
        articleArrayList = new ArrayList<>();
        newsReceiver = new NewsReceiver();
        fragments = new ArrayList<>();
        pageAdapter = new FragmentAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
        Intent serviceIntent = new Intent(this, NewsService.class);
        startService(serviceIntent);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Sources temp = sourceArrayList.get(position);
                        Intent intent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
                        intent.putExtra(SOURCE, temp);
                        sendBroadcast(intent);
                        setTitle(temp.getName());
                        Name = temp.getName();
                        category = temp.getCategory();
                        newsId = temp.getId();
                        pager.setVisibility(View.VISIBLE);
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );
        if(isNetworkAvailable(this))
        {
            SourcesRunnable sourcesRunnable = new SourcesRunnable(this);
            new Thread(sourcesRunnable).start();
        }
        if(sourcesData.isEmpty()){
            SourcesRunnable sourcesRunnable = new SourcesRunnable(this);
            new Thread(sourcesRunnable).start();
        }
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);
    }

    public void setupCategory(HashMap<String, ArrayList<Sources>> sourcesMapIn) {
        sourcesData.clear();
        try
        {
            menu.clear();
            sourcesData = sourcesMapIn;
            int i = 0;
            ArrayList<String> tempList = new ArrayList<>(sourcesData.keySet());
            Collections.sort(tempList);
            for(String category: tempList)
            {
                menu.add((category));
            }
            sourceArrayList.addAll(Objects.requireNonNull(sourcesData.get("all")));
            mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item,sourceArrayList));
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "setupCategory: catchBlock");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        setTitle(item.getTitle().toString());
        sourceArrayList.clear();
        ArrayList<Sources> drawerList = sourcesData.get(item.getTitle().toString().toLowerCase());
        if(drawerList != null)
        {
            sourceArrayList.addAll(drawerList);
        }
        ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {

        super.onSaveInstanceState(outState);
        outState.putInt("POS",position);
        outState.putString("NAME", Name);
        outState.putString("CATEGORY", category);
        outState.putString("ID", newsId);

    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(newsReceiver);
        Intent i = new Intent(this,NewsService.class);
        stopService(i);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("POS");
        Name = savedInstanceState.getString("NAME");
        category = savedInstanceState.getString("CATEGORY");
        newsId = savedInstanceState.getString("ID");
    }

    @Override
    protected void onResume()
    {
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);
        super.onResume();
    }


    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return networkInfo.isConnected();
        }
        else{
            return false;
        }
    }

    public void setFragments(int a)
    {
        if ( a == 0){
            for (int i = 0; i < pageAdapter.getCount(); i++) {
                pageAdapter.notifyChangeInPosition(i);
                position = i;
            }
        }
        fragments.clear();
        for (int i = 0; i < articleArrayList.size(); i++)
        {
            fragments.add(ArticleFragment.newInstance(articleArrayList.get(i), i+1, articleArrayList.size()));
        }
        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }
    public void showToast() {
        Toast.makeText(this, "Wrong Address Entered", Toast.LENGTH_LONG).show();
    }

    private class FragmentAdapter extends FragmentPagerAdapter
    {
        private long baseId = 0;
        FragmentAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public int getItemPosition(@NonNull Object object)
        {
            return POSITION_NONE;
        }
        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }
        @Override
        public int getCount()
        {
            return fragments.size();
        }
        @Override
        public long getItemId(int position)
        {
            return baseId + position;
        }
        void notifyChangeInPosition(int n)
        {
            baseId += getCount() + n;
        }
    }

    class NewsReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            articleArrayList.clear();
            if (intent.hasExtra(ARTICLE_LIST))
            {
                articleArrayList = (ArrayList<Article>) intent.getSerializableExtra(ARTICLE_LIST);
                setFragments(0);
            }
        }
    }

}