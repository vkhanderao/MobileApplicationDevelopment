package com.example.android.stockAssistant;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder {

    public TextView stockSymbol;
    public TextView companyName;
    public TextView companyPrice;
    public TextView companyPriceChange;
    public TextView percentageChange;
    public ImageView arrow;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        stockSymbol = itemView.findViewById(R.id.stockSymbol_label);
        companyName = itemView.findViewById(R.id.companyName);
        companyPrice = itemView.findViewById(R.id.stockPriceValue);
        companyPriceChange = itemView.findViewById(R.id.stockPriceChange);
        percentageChange = itemView.findViewById(R.id.stockPercentPriceChgVal);
        arrow = itemView.findViewById(R.id.imageView);
    }
}
