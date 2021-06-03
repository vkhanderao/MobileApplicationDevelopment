package com.akshay.knowyourgovernment;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class runnableGetData implements Runnable{
    private MainActivity mainActivity;
    private String address;
    private static final String TAG = "runnableGetData";
    private String urlBegin="https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private String urlEnd="AIzaSyCnpkITQ4yQcXRbgy7JVZsZWNreGpRZxNg&address=";

    public runnableGetData(MainActivity mainActivity, String address) {
        this.mainActivity = mainActivity;
        this.address = address;
    }

    @Override
    public void run() {
        String finalURl=urlBegin+urlEnd+address;
        Uri dataUri=Uri.parse(finalURl);
        String urlToUse = dataUri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            Log.e(TAG, "run:Try Block ");
            URL url = new URL(urlToUse);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "run: " + sb.toString());
            handleResults(sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run:Catch Block ", e);
        }
    }

    private void handleResults(String S){
        if(S==null){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.showToast();
                }
            });
        }

        parseJson(S);
    }
    private void parseJson(String jsonString) {
        try {
            JSONObject baseObject = new JSONObject(jsonString);
            JSONObject normalizedAddress = baseObject.getJSONObject("normalizedInput");
            final String selectedAddressDetails = normalizedAddress.getString("city") + ", " + normalizedAddress.getString("state") + " " + normalizedAddress.getString("zip");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.setAddress(selectedAddressDetails.trim());
                }
            });
            JSONArray officeDetails = baseObject.getJSONArray("offices");
            JSONArray officialsDetails = baseObject.getJSONArray("officials");
            final ArrayList<electorPersonMethod> electorPersonList = new ArrayList<>();
            int i = 0;
            while (i < officeDetails.length()) {
                JSONObject eachOffice = officeDetails.getJSONObject(i);
                String eachOfficeName = eachOffice.getString("name");
                JSONArray eachOfficeOfficialIndices = eachOffice.getJSONArray("officialIndices");
                for (int j = 0; j < eachOfficeOfficialIndices.length(); j++) {
                    int index = eachOfficeOfficialIndices.getInt(j);
                    JSONObject eachOfficialIndex = officialsDetails.getJSONObject(index);
                    electorPersonMethod eachElectedPersonObject = new electorPersonMethod(eachOfficialIndex.getString("name"), eachOfficeName, eachOfficialIndex.getString("party"));
                    if(eachOfficialIndex.has("address")) {
                        JSONObject address = eachOfficialIndex.getJSONArray("address").getJSONObject(0);
                        eachElectedPersonObject.setAddress(convertObjectToStringAddress(address));
                    }
                    if(eachOfficialIndex.has("phones")) {
                        eachElectedPersonObject.setPhone(eachOfficialIndex.getJSONArray("phones").getString(0));
                    }
                    if(eachOfficialIndex.has("urls")) {
                        eachElectedPersonObject.setWebsite(eachOfficialIndex.getJSONArray("urls").getString(0));
                    }
                    if(eachOfficialIndex.has("emails")) {
                        eachElectedPersonObject.setEmail(eachOfficialIndex.getJSONArray("emails").getString(0));
                    }
                    if(eachOfficialIndex.has("photoUrl")) {
                        eachElectedPersonObject.setPhotoUrl(eachOfficialIndex.getString("photoUrl"));
                    }
                    if(eachOfficialIndex.has("channels")) {
                        JSONArray channelArrray = eachOfficialIndex.getJSONArray("channels");
                        for (int k = 0; k < channelArrray.length(); k++) {
                            JSONObject singleChannel = channelArrray.getJSONObject(k);
                            switch (singleChannel.getString("type")) {
                                case "Facebook":
                                    eachElectedPersonObject.setFacebook(singleChannel.getString("id"));
                                    break;
                                case "Twitter":
                                    eachElectedPersonObject.setTwitter(singleChannel.getString("id"));
                                    break;
                                case "YouTube":
                                    eachElectedPersonObject.setYoutube(singleChannel.getString("id"));
                                    break;
                            }
                        }
                    }
                    electorPersonList.add(eachElectedPersonObject);
                }
                i++;
            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.updateOfficialList(electorPersonList);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String convertObjectToStringAddress(JSONObject address){
        StringBuilder sb = new StringBuilder();
        try {
            if(address.has("line1")) {
                sb.append(address.getString("line1"));
                sb.append(", ");
            }
            if(address.has("line2")) {
                sb.append(address.getString("line2"));
                sb.append(", ");
            }
            if(address.has("line3")) {
                sb.append(address.getString("line3"));
                sb.append(", ");
            }
            sb.append(address.getString("city"));
            sb.append(", ");
            sb.append(address.getString("state"));
            sb.append(" ");
            sb.append(address.getString("zip"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }


}
