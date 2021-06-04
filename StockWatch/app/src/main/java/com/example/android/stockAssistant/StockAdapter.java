package com.example.android.stockAssistant;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<StockDetails> stockList;
    private MainActivity mainActivity;

    public StockAdapter(List<StockDetails> sList,MainActivity mActivity)
    {
        this.stockList = sList;
        this.mainActivity = mActivity;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_details,parent,false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        StockDetails sd = stockList.get(position);
        if(sd.getChangePercentage() > 0) {
            holder.stockSymbol.setTextColor(Color.GREEN);
            holder.companyName.setTextColor(Color.GREEN);
            holder.companyPrice.setTextColor(Color.GREEN);
            holder.companyPriceChange.setTextColor(Color.GREEN);
            holder.percentageChange.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.baseline_arrow_drop_up_black_18dp);
            holder.arrow.setColorFilter(Color.GREEN);
        }
        else
        {
            holder.stockSymbol.setTextColor(Color.RED);
            holder.companyName.setTextColor(Color.RED);
            holder.companyPrice.setTextColor(Color.RED);
            holder.companyPriceChange.setTextColor(Color.RED);
            holder.percentageChange.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.baseline_arrow_drop_down_black_18dp);
            holder.arrow.setColorFilter(Color.RED);
        }
        holder.stockSymbol.setText(sd.getStockSymbol());
        holder.companyName.setText(sd.getCmpName());
        holder.companyPrice.setText(String.format(Locale.US, "%.2f", sd.getPrice()));
        holder.companyPriceChange.setText(String.format(Locale.US, "%.2f", sd.getPriceChange()));
        holder.percentageChange.setText((String.format(Locale.US, "(%.2f%%)", sd.getChangePercentage())));
    }
    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
