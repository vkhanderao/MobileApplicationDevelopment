package com.akshay.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class officialActivity extends AppCompatActivity {
    private static final String TAG = "officialActivity";
    electorPersonMethod electorPesron;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        TextView defaultLocation=findViewById(R.id.fillLocationView);
        TextView officePositionView=findViewById(R.id.officePositionView);
        TextView officialNameView=findViewById(R.id.officialNameView);
        TextView partyNameView=findViewById(R.id.partyNameView);
        ImageView partyButton=findViewById(R.id.partyButton);
        defaultLocation.setText(getIntent().getStringExtra("currentLocation"));
        electorPesron=(electorPersonMethod)  getIntent().getSerializableExtra("electorPersonMethodObject");
        officePositionView.setText(electorPesron.getOffice());
        officialNameView.setText(electorPesron.getName());
        partyNameView.setText(electorPesron.getParty());
        if(electorPesron.containsPhotoUrl()){
           loadImage();
        }
        ConstraintLayout layout = findViewById(R.id.parentLayout);
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
        handleChannels();
        handleOptionalViews();
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

    public void photoClicked(View v) {
        if(electorPesron.containsPhotoUrl()) {
        TextView defaultLocation=findViewById(R.id.fillLocationView);
        Intent intent = new Intent(this, photoActivity.class);
        intent.putExtra("electorPersonMethodObject", electorPesron);
        intent.putExtra("currentLocation",  defaultLocation.getText().toString());
        startActivity(intent);
        }
    }
    public void facebookClicked(View v){
        String officialUserName = electorPesron.getFacebook();
        String FACEBOOK_URL = "https://www.facebook.com/" + officialUserName;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + officialUserName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlToUse));
        startActivity(intent);
    }
    public void twitterClicked(View v){
        Intent intent = null;
        String name = electorPesron.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }
    public void youtubeClicked(View v){
        String name = electorPesron.getYoutube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
    public void loadImage(){
        ImageView officialImage=findViewById(R.id.officialImage);
        Picasso.get().load(electorPesron.getPhotoUrl())
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(officialImage);
    }

    private void handleChannels() {
        if(!electorPesron.containsFacebook()) {
            ImageView facebook = findViewById(R.id.facebookbutton);
            facebook.setVisibility(View.INVISIBLE);
        }
        if(!electorPesron.containsTwitter()) {
            ImageView twitter = findViewById(R.id.twitterbutton);
            twitter.setVisibility(View.INVISIBLE);
        }
        if(!electorPesron.containsYoutube()) {
            ImageView youtube = findViewById(R.id.youtubeButton);
            youtube.setVisibility(View.INVISIBLE);
        }

    }

    private void handleOptionalViews() {
        TextView emailLabel, fillEmail,websiteLabel, fillWebsite,addressLabel, fillAddress,phoneLabel, fillPhone;
        emailLabel = findViewById(R.id.emailView);
        fillEmail = findViewById(R.id.fillEmailView);
        if(electorPesron.containsEmail()) {
            fillEmail.setText(electorPesron.getEmail());
            Linkify.addLinks(fillEmail,Linkify.ALL);
        } else {
            emailLabel.setVisibility(View.INVISIBLE);
            fillEmail.setVisibility(View.INVISIBLE);
        }
        websiteLabel = findViewById(R.id.websiteView);
        fillWebsite = findViewById(R.id.fillWebsiteView);
        if(electorPesron.containsWebsite()) {
            fillWebsite.setText(electorPesron.getWebsite());
            Linkify.addLinks(fillWebsite,Linkify.ALL);
        } else {
            websiteLabel.setVisibility(View.INVISIBLE);
            fillWebsite.setVisibility(View.INVISIBLE);
        }

        addressLabel = findViewById(R.id.addressView);
        fillAddress = findViewById(R.id.filladdressView);
        if(electorPesron.containsAddress()) {
            fillAddress.setText(electorPesron.getAddress());
            Linkify.addLinks(fillAddress,Linkify.ALL);
        } else {
            addressLabel.setVisibility(View.INVISIBLE);
            fillAddress.setVisibility(View.INVISIBLE);
        }

        phoneLabel = findViewById(R.id.phoneView);
        fillPhone = findViewById(R.id.fillPhoneView);
        if(electorPesron.containsPhoneNumber()) {
            fillPhone.setText(electorPesron.getPhone());
            Linkify.addLinks(fillPhone,Linkify.ALL);
        } else {
            phoneLabel.setVisibility(View.INVISIBLE);
            fillPhone.setVisibility(View.INVISIBLE);
        }
    }
}