package com.akshay.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private electedPersonListAdapter eplAdapter;
    private final ArrayList<electorPersonMethod> eleactorList= new ArrayList<>();
    private TextView locationTextView;
    private FusedLocationProviderClient fusLocation;
    private static String locationString = "Unspecified Location";
    private static final int LOCATION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationTextView=findViewById(R.id.locationTextView);
        eplAdapter=new electedPersonListAdapter(this,eleactorList);
        recyclerView.setAdapter(eplAdapter);
        fusLocation= LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
    }


    ///options menu creation Function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }


    ///check Network Connection
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return networkInfo.isConnected();
        }
        else{
            return false;
        }
    }


    ///options menu Click function
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutIcon:
                Intent intent = new Intent(this, aboutActivity.class);
                startActivity(intent);
                break;
            case R.id.searchMenu:
                Intent intenttemp = new Intent(this, aboutActivity.class);
                if(isNetworkAvailable(this)){
                    //Network Present
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enter Address");
                    final EditText addrestext= new EditText(this);
                    addrestext.setInputType(InputType.TYPE_CLASS_TEXT);
                    addrestext.setInputType(Gravity.CENTER_HORIZONTAL);
                    builder.setView(addrestext);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runnableGetData  dataGetRunnable = new runnableGetData (  MainActivity.this, addrestext.getText().toString().trim());
                            new Thread(dataGetRunnable).start();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Cancel Pressed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();

                }
                else{
                    noNetworkAlert();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    ///No network Alert
    public void noNetworkAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("No Network Connection");
        alert.setMessage("Data cannot be accessed/loaded without an internet connection");
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int pos=recyclerView.getChildLayoutPosition(v);
        electorPersonMethod eleList=eleactorList.get(pos);
        Intent intent=new Intent(this,officialActivity.class);
        intent.putExtra("electorPersonMethodObject",eleList);
        intent.putExtra("currentLocation", locationTextView.getText().toString());
        startActivity(intent);
    }

    void updateOfficialList(List<electorPersonMethod> electorPersonList) {
        this.eleactorList.clear();
        this.eleactorList.addAll(electorPersonList);
        eplAdapter.notifyDataSetChanged();
    }

    public void setAddress(String address) {
        locationTextView.setText(address);
    }
    //Temp remove if not needed
    public void showToast() {
        Toast.makeText(this, "Wrong Address Entered", Toast.LENGTH_LONG).show();
    }

    private void determineLocation() {
        if (checkPermission()) {
            fusLocation.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            locationString = getPlace(location);
                            locationTextView.setText(locationString);
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s%n%nProvider: %s%n%n%.5f, %.5f",
                    city, state, loc.getProvider(), loc.getLatitude(), loc.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    locationTextView.setText(R.string.deniedText);
                }
            }
        }
    }
}