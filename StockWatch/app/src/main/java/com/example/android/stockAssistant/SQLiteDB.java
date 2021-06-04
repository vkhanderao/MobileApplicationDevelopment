package com.example.android.stockAssistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteDB";
    private static final String DATABASE_NAME = "StockAssistantDB";
    private static final String TABLE_NAME = "StockAssistant";
    private static final String SYMBOL = "StockSymbol";
    private static final String CNAME = "CompanyName";
    private static final int DB_VERSION = 1;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + SYMBOL + " TEXT not null unique," + CNAME + " TEXT not null)";

    private SQLiteDatabase database;

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.d(TAG, "onCreate: Created Table" + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<StockDetails> loadStocks() {
        ArrayList<StockDetails> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME, // The table to query
                new String[]{SYMBOL, CNAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            int i = 0;
            while(i < cursor.getCount()){
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                StockDetails sd = new StockDetails();
                sd.setStockSymbol(symbol);
                sd.setCmpName(name);
                sd.setPrice(0.0);
                sd.setPriceChange(0.0);
                sd.setChangePercentage(0.0);
                stocks.add(sd);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        }
        return stocks;
    }

    public void addStock(StockDetails newStock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, newStock.getStockSymbol());
        values.put(CNAME, newStock.getCmpName());
        long key = database.insert(TABLE_NAME, null, values);
    }

    public void deleteStock(String symbol) {
        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{symbol});
    }

    public void shutDown() {
        database.close();
    }
}
