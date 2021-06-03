package com.akshay.knowyourgovernment;

import java.io.Serializable;
import java.util.HashMap;

public class electorPersonMethod implements Serializable {

    private String name;
    private String office;
    private String party;
    private String address;
    private String phone;
    private String website;
    private String email;
    private String facebook;
    private String youtube;
    private String twitter;
    private String photoUrl;


    public electorPersonMethod(String name, String office, String party) {
        this.name = name;
        this.office = office;
        this.party = party;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public String getParty() {
        return party;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public boolean containsAddress() {
        return address != null && !address.isEmpty();
    }

    public boolean containsPhoneNumber() {
        return phone != null && !phone.isEmpty();
    }

    public boolean containsWebsite() {
        return website != null && !website.isEmpty();
    }

    public boolean containsEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean containsFacebook() {
        return facebook != null && !facebook.isEmpty();
    }

    public boolean containsYoutube() {
        return youtube != null && !youtube.isEmpty();
    }

    public boolean containsTwitter() {
        return twitter != null && !twitter.isEmpty();
    }

    public boolean containsPhotoUrl() {
        return photoUrl != null && !photoUrl.isEmpty();
    }
}
