package com.example.android.stockAssistant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private StockAdapter sAdapter;
    private  SQLiteDB database;
    SwipeRefreshLayout swipe;

    private final List<StockDetails> stockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        swipe = findViewById(R.id.swipeContainer);
        sAdapter = new StockAdapter(stockList,this);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = new SQLiteDB(this);
        ArrayList<StockDetails> tempStockList = database.loadStocks();

        if(networkCheck()){
            for(int i=0;i<tempStockList.size();i++)
            {
                String symbol = tempStockList.get(i).getStockSymbol();
                new StockDownloader(this).execute(symbol);
            }
        }
        else
        {
            errorDialog();
            stockList.addAll(tempStockList);
            Collections.sort(stockList, new Comparator<StockDetails>() {
                @Override
                public int compare(StockDetails o1, StockDetails o2) {
                    return o1.getStockSymbol().compareTo(o2.getCmpName());
                }
            });
            sAdapter.notifyDataSetChanged();
        }


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkCheck()) {
                    swipe.setRefreshing(false);
                    stockList.clear();
                    ArrayList<StockDetails> list = database.loadStocks();
                    int i = 0;
                    while(i < list.size()){
                        String symbol = list.get(i).getStockSymbol();
                        new StockDownloader(MainActivity.this).execute(symbol);
                        i++;
                    }
                }
                else {
                    swipe.setRefreshing(false);
                    errorDialog();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.add_item_button)
        {
            addNewStock();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int childPosition = recyclerView.getChildLayoutPosition(view);
        String marketPlaceURL = "http://www.marketwatch.com/investing/stock/" + stockList.get(childPosition).getStockSymbol();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(marketPlaceURL));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        final int id = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol " + ((TextView) view.findViewById(R.id.stockSymbol_label)).getText().toString() + "?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteStock(stockList.get(id).getStockSymbol());
                stockList.remove(id);
                sAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Stock Deleted", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }


    public void addNewStock()
    {
        if(networkCheck())
        {
            final EditText enterSymbolEditText = new EditText(this);
            enterSymbolEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            enterSymbolEditText.setGravity(Gravity.CENTER_HORIZONTAL);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Stock Selection")
                    .setMessage("Please enter a Stock Symbol:")
                    .setView(enterSymbolEditText)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String symbol = String.valueOf(enterSymbolEditText.getText());
                           getStockSymbols(symbol);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }
        else
        {
            errorDialog();
        }
    }

    public void errorDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Connection Unavailable");
        builder.setMessage("Network Connection to add Stocks");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean networkCheck()
    {
        ConnectivityManager connManger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManger == null)
        {
            Toast.makeText(this,"Cannot access ConnectivityManager", Toast.LENGTH_LONG).show();
            return false;
        }
        NetworkInfo netInformation = connManger.getActiveNetworkInfo();
        return (netInformation != null && netInformation.isConnected());
    }

    public void getStockSymbols(String s) { new NameDownloader(this).execute(s); }

    public void StockSymbols(String s, final HashMap<String,String> result)
    {
        if(result.size() == 1)  {
            enterDetails(s);
        }
        else if (result.size() > 1){
            final String[] res = new String[result.size()];
            int i = 0;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");
            for (Map.Entry<String,String> entry : result.entrySet())
            {
                String display = entry.getKey() + " - " + entry.getValue();
                res[i] = display;
                i++;
            }
            builder.setItems(res, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String sym[] = res[i].split(" ");
                    String symbol = sym[0];
                    enterDetails(symbol);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (result.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Symbol Not Found: " + s);
            builder.setMessage("Data for stock symbol");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void stockData(StockDetails stock)
    {
        if (stock != null) {
            Log.d(TAG, "In Stock !=null condition");
            int index = stockList.indexOf(stock);
            Log.d(TAG, "The index " + index);
            if (index > -1) {
                Log.d(TAG, "In Stock index");
                stockList.remove(index);
            }
            stockList.add(stock);
            Collections.sort(stockList, new Comparator<StockDetails>() {
                @Override
                public int compare(StockDetails o1, StockDetails o2) {
                    return o1.getStockSymbol().compareTo(o2.getCmpName());
                }
            });
            sAdapter.notifyDataSetChanged();
        }
    }

    public void addStockDetailsToDB(StockDetails sd)
    {
        boolean duplicate = false;
        if (sd != null) {
            StockDetails sd1 = new StockDetails();
            sd1.setStockSymbol(sd.getStockSymbol());
            sd1.setCmpName(sd.getCmpName());

            if(stockList.size() < 0){
                stockList.add(sd);
            }
            else
            {
                for(StockDetails s : stockList)
                {
                    if((s.getStockSymbol()).equals(sd.getStockSymbol())) {
                        duplicate = true;
                        break;
                    }
                }

                if(duplicate != true)
                {
                    stockList.add(sd);
                    database.addStock(sd1);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Duplicate Stock");
                    builder.setMessage("Stock Symbol " + sd.getStockSymbol() +" is already displayed");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            database.addStock(sd1);
        }
        Collections.sort(stockList, new Comparator<StockDetails>() {
            @Override
            public int compare(StockDetails o1, StockDetails o2) {
                return o1.getStockSymbol().compareTo(o2.getStockSymbol());
            }
        });
        sAdapter.notifyDataSetChanged();
    }

    public void enterDetails(String s)
    {
        new StockDownloader(this).execute(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.shutDown();
    }


}
