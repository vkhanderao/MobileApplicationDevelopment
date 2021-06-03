package com.akshay.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class aboutActivity extends AppCompatActivity {
    public static final String TAG="AboutActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView hyperLink=findViewById(R.id.aboutDataProvider);
        setTitle(R.string.aboutPageTitle);
        String underlineData=getString(R.string.dataProviderName);
        SpannableString sp = new SpannableString(underlineData);
        sp.setSpan(new UnderlineSpan(), 0, underlineData.length(), 0);
        hyperLink.setText(sp);
        Uri dataUri=Uri.parse("https://developers.google.com/civic-information");
        Log.d(TAG, "onCreate:");
        hyperLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW,dataUri);
                if (intent.resolveActivity((getPackageManager()))!=null)
                    startActivity(intent);
            }
        });
    }
}