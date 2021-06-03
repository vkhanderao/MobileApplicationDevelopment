package com.akshay.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private EditText totalBillWithoutTip;
    private EditText numberOfPersons;
    private TextView tipAmount;
    private TextView totalBillWithTip;
    private TextView billPerPerson;
    private TextView overage;

    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalBillWithoutTip=findViewById(R.id.enterBillAmount);
        numberOfPersons=findViewById(R.id.numberOfPeople);
        totalBillWithTip=findViewById(R.id.totalBillWithTip);
        tipAmount=findViewById(R.id.tipAmount);
        billPerPerson=findViewById(R.id.costPerPerson);
        overage=findViewById(R.id.remainder);
    }

    public void calculateTipAmount(View v){
        Log.d(TAG, "calculateTipAmount: ");
        String bill=totalBillWithoutTip.getText().toString();
        if (bill.trim().isEmpty()){
            ((RadioButton) findViewById(R.id.select12PercentTip)).setChecked(false);
            ((RadioButton) findViewById(R.id.select15PercentTip)).setChecked(false);
            ((RadioButton) findViewById(R.id.select18PercentTip)).setChecked(false);
            ((RadioButton) findViewById(R.id.select20PercentTip)).setChecked(false);
            tipAmount.setText("");
            totalBillWithTip.setText("");
        }
        else{
            double billAmount=Integer.parseInt(bill);
            double tipvalue=0;
            if(v.getId()==R.id.select12PercentTip){
                //billAmount=Integer.parseInt(bill);
                tipvalue=percentageAmount(billAmount,12);
            }
            else if(v.getId()==R.id.select15PercentTip){
                //billAmount=Integer.parseInt(bill);
                tipvalue=percentageAmount(billAmount,15);
            }
            else if(v.getId()==R.id.select18PercentTip){
                //billAmount=Integer.parseInt(bill);
                tipvalue=percentageAmount(billAmount,18);
            }
            else if(v.getId()==R.id.select20PercentTip){
                //billAmount=Integer.parseInt(bill);
                tipvalue=percentageAmount(billAmount,20);
            }
            tipAmount.setText(Double.toString(tipvalue));
            totalBillWithTip.setText(String.valueOf((tipvalue+billAmount)));
        }
    }

    public void calculateSplitOverage(View v){
        Log.d(TAG, "calculateSplitOverage: ");
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        double bill=Double.parseDouble(totalBillWithTip.getText().toString());
        int peopleCount= Integer.parseInt(numberOfPersons.getText().toString());
        double amountCalc=Double.parseDouble(df.format(bill/peopleCount));
        billPerPerson.setText(Double.toString(amountCalc));
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        int numberofPeople=Integer.parseInt(numberOfPersons.getText().toString());
        double textTemp=(amountCalc*5)-bill;
        overage.setText(df.format((amountCalc*numberofPeople)-bill));
    }
    
    public void clearAll(View v){
        Log.d(TAG, "clearAll: ");
        totalBillWithoutTip.setText("");
        totalBillWithTip.setText("");
        numberOfPersons.setText("");
        tipAmount.setText("");
        billPerPerson.setText("");
        overage.setText("");;
        ((RadioButton) findViewById(R.id.select12PercentTip)).setChecked(false);
        ((RadioButton) findViewById(R.id.select15PercentTip)).setChecked(false);
        ((RadioButton) findViewById(R.id.select18PercentTip)).setChecked(false);
        ((RadioButton) findViewById(R.id.select20PercentTip)).setChecked(false);
    }

    public double percentageAmount(double value,double percentage){
        return (value*percentage)/100;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("totalBillWithoutTip", totalBillWithoutTip.getText().toString());
        outState.putString("tipAmount", tipAmount.getText().toString());
        outState.putString("totalBillWithTip", totalBillWithTip.getText().toString());
        outState.putString("billPerPerson", billPerPerson.getText().toString());
        outState.putString("overage", overage.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        totalBillWithoutTip.setText(savedInstanceState.getString("totalBillWithoutTip"));
        tipAmount.setText(savedInstanceState.getString("tipAmount"));
        totalBillWithTip.setText(savedInstanceState.getString("totalBillWithTip"));
        billPerPerson.setText(savedInstanceState.getString("billPerPerson"));
        overage.setText(savedInstanceState.getString("overage"));
    }
}