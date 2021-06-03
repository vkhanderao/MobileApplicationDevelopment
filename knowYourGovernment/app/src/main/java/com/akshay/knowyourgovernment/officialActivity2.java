package com.akshay.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class officialActivity2 extends AppCompatActivity {
    private static final String TAG = "officialActivity2";
    electorPersonMethod electorPesron;
    TextView defaultLocation;
    TextView officePositionView;
    TextView officialNameView;
    TextView partyNameView;
    TextView filladdressView;
    TextView fillPhoneView;
    TextView fillWebsiteView;
    TextView fillEmailView;
    ImageView officialImage;
    ImageButton partyButton;
    ImageButton facebookbutton;
    ImageButton twitterbutton;
    ImageButton youtubeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official2);
        TextView defaultLocation = findViewById(R.id.fillLocationView1);
        TextView officePositionView = findViewById(R.id.officePositionView1);
        TextView officialNameView = findViewById(R.id.officialNameView1);
        TextView partyNameView = findViewById(R.id.partyNameView1);
        TextView filladdressView = findViewById(R.id.filladdressView1);
        TextView fillPhoneView = findViewById(R.id.fillPhoneView1);
        TextView fillWebsiteView = findViewById(R.id.fillWebsiteView1);
        TextView fillEmailView = findViewById(R.id.fillEmailView1);
        ConstraintLayout layout = findViewById(R.id.parentLayoutbottom);
        ConstraintLayout layout1 = findViewById(R.id.parentLayoutTop);
        ImageButton partyButton = findViewById(R.id.partyButton1);
        ImageButton facebookbutton = findViewById(R.id.facebookbutton1);
        ImageButton twitterbutton = findViewById(R.id.twitterbutton1);
        ImageButton youtubeButton = findViewById(R.id.youtubeButton1);
        defaultLocation.setText(getIntent().getStringExtra("currentLocation"));
        electorPesron = (electorPersonMethod) getIntent().getSerializableExtra("electorPersonMethodObject");
        officePositionView.setText(electorPesron.getOffice());
        officialNameView.setText(electorPesron.getName());
        partyNameView.setText(electorPesron.getParty());
        if (electorPesron.containsPhotoUrl()) {
            loadImage();
        } else {
            ImageView officialImage1 = findViewById(R.id.officialImage1);
            officialImage1.setImageDrawable(getDrawable(R.drawable.missing));

        }

        if (electorPesron.getParty().equalsIgnoreCase("Republican Party")){
            layout.setBackgroundColor(Color.RED);
            layout1.setBackgroundColor(Color.RED);
            partyButton.setImageResource(R.drawable.rep_logo);
        }
        if (electorPesron.getParty().equalsIgnoreCase("Democratic Party")){
            layout.setBackgroundColor(Color.BLUE);
            layout1.setBackgroundColor(Color.BLUE);
            partyButton.setImageResource(R.drawable.dem_logo);
        }
        if (electorPesron.getParty().equalsIgnoreCase("Nonpartisan")){
            layout.setBackgroundColor(Color.BLACK);
            layout1.setBackgroundColor(Color.BLACK);
            partyButton.setVisibility(View.INVISIBLE);
        }
        handleChannels();
        handleOptionalViews();
    }

    public void loadImage(){
        ImageView officialImage=findViewById(R.id.officialImage1);
        Picasso.get().setLoggingEnabled(true);
        String url=electorPesron.getPhotoUrl();
        Picasso.get().load(url)
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
    public void facebookClicked(View v){

    }
    public void twitterClicked(View v){

    }
    public void youtubeClicked(View v){

    }

    private void handleChannels() {
        if(!electorPesron.containsFacebook()) {
            ImageButton facebook = findViewById(R.id.facebookbutton1);
            facebook.setVisibility(View.INVISIBLE);

        }
        ImageButton facebook = findViewById(R.id.facebookbutton1);
        facebook.setImageDrawable(getDrawable(R.drawable.facebook));
        if(!electorPesron.containsTwitter()) {
            ImageButton twitter = findViewById(R.id.twitterbutton1);
            twitter.setVisibility(View.INVISIBLE);

        }
        ImageButton twitter = findViewById(R.id.twitterbutton1);
        twitter.setImageDrawable(getDrawable(R.drawable.twitter));
        if(!electorPesron.containsYoutube()) {
            ImageButton youtube = findViewById(R.id.youtubeButton1);
            youtube.setVisibility(View.INVISIBLE);

        }
        ImageButton youtube = findViewById(R.id.youtubeButton1);
        youtube.setImageDrawable(getDrawable(R.drawable.youtube));
    }

    private void handleOptionalViews() {
        TextView emailLabel, fillEmail,websiteLabel, fillWebsite,addressLabel, fillAddress,phoneLabel, fillPhone;
        emailLabel = findViewById(R.id.emailView1);
        fillEmail = findViewById(R.id.fillEmailView1);
        if(electorPesron.containsEmail()) {
            fillEmail.setText(electorPesron.getEmail());
        } else {
            emailLabel.setVisibility(View.INVISIBLE);
            fillEmail.setVisibility(View.INVISIBLE);
        }
        websiteLabel = findViewById(R.id.websiteView1);
        fillWebsite = findViewById(R.id.fillWebsiteView1);
        if(electorPesron.containsWebsite()) {
            fillWebsite.setText(electorPesron.getWebsite());
        } else {
            websiteLabel.setVisibility(View.INVISIBLE);
            fillWebsite.setVisibility(View.INVISIBLE);
        }

        addressLabel = findViewById(R.id.addressView1);
        fillAddress = findViewById(R.id.filladdressView1);
        if(electorPesron.containsAddress()) {
            fillAddress.setText(electorPesron.getAddress());
        } else {
            addressLabel.setVisibility(View.INVISIBLE);
            fillAddress.setVisibility(View.INVISIBLE);
        }

        phoneLabel = findViewById(R.id.phoneView1);
        fillPhone = findViewById(R.id.fillPhoneView1);
        if(electorPesron.containsPhoneNumber()) {
            fillPhone.setText(electorPesron.getPhone());
        } else {
            phoneLabel.setVisibility(View.INVISIBLE);
            fillPhone.setVisibility(View.INVISIBLE);
        }
    }

}