package com.example.android.stockAssistant;

import java.io.Serializable;

public class StockDetails implements Serializable {

    private String stockSymbol;
    private String companyName;
    private double companyPrice;
    private double priceChange;
    private double changePercentage;


    public void setStockSymbol(String stockSymbol)
    {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol()
    {
        return stockSymbol;
    }

    public void setCmpName(String cmpName)
    {
        this.companyName = cmpName;
    }

    public String getCmpName()
    {
        return companyName;
    }

    public void setPrice(double companyPrice)
    {
        this.companyPrice = companyPrice;
    }

    public double getPrice()
    {
        return companyPrice;
    }

    public void setPriceChange(double priceChange)
    {
        this.priceChange = priceChange;
    }

    public double getPriceChange()
    {
        return priceChange;
    }

    public void setChangePercentage(double changePercentage)
    {
        this.changePercentage = changePercentage;
    }

    public double getChangePercentage()
    {
        return changePercentage;
    }



}
