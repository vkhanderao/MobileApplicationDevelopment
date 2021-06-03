package com.akshay.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class photoActivity extends AppCompatActivity {

    private static final String TAG = "officialActivity";
    electorPersonMethod electorPesron;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        TextView defaultLocation=findViewById(R.id.defaultLocationPhoto);
        defaultLocation.setText(getIntent().getStringExtra("currentLocation"));
        electorPesron=(electorPersonMethod)  getIntent().getSerializableExtra("electorPersonMethodObject");
        ImageView partyButton=findViewById(R.id.partyLogoPhoto);
        if(electorPesron.containsPhotoUrl()){
            loadImage();
        }

        TextView officePosition = findViewById(R.id.officePositionPhoto);
        TextView officialName = findViewById(R.id.officialNamePhoto);
        officePosition.setText(electorPesron.getOffice());
        officialName.setText(electorPesron.getName());
        officialName.setText(electorPesron.getName());
        ConstraintLayout layout = findViewById(R.id.parentLayoutPhoto);
        if (electorPesron.getParty().equalsIgnoreCase("Republican Party")){
            layout.setBackgroundColor(Color.RED);
            partyButton.setImageResource(R.drawable.rep_logo);
        }
        if (electorPesron.getParty().equalsIgnoreCase("Democratic Party")){
            layout.setBackgroundColor(Color.BLUE);
            partyButton.setImageResource(R.drawable.dem_logo);
        }
        if (electorPesron.getParty().equalsIgnoreCase("Nonpartisan")){
            layout.setBackgroundColor(Color.BLACK);
            partyButton.setVisibility(View.INVISIBLE);
        }
    }


    public void loadImage(){
        ImageView officialImage=findViewById(R.id.photoofficialPhoto);
        Picasso.get().load(electorPesron.getPhotoUrl())
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(officialImage);
    }

    public void partyClicked(View v){
        String name = electorPesron.getParty();
        if(name.equalsIgnoreCase("Democratic Party")) {
            Uri dataUri=Uri.parse("https://democrats.org/");
            Intent i = new Intent(Intent.ACTION_VIEW, dataUri);
            startActivity(i);
            return;
        }

        if(name.equalsIgnoreCase("Republican Party")) {
            Uri dataUri=Uri.parse("https://www.gop.com/");
            Intent i = new Intent(Intent.ACTION_VIEW, dataUri);
            startActivity(i);
            return;
        }
    }

}