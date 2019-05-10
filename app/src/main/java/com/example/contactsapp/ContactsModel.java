package com.example.contactsapp;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ContactsModel implements Searchable {

    private String mName;
    private String mPhoneNumber;
    private String mEmail;
    private String mUri;
    private String mThumnail_uri;

    public ContactsModel(String mName, String mPhoneNumber, String mEmail, String mUri, String mThumnail_uri) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
        this.mUri = mUri;
        this.mThumnail_uri = mThumnail_uri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUri() {
        return mUri;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public String getmThumnail_uri() {
        return mThumnail_uri;
    }

    public void setmThumnail_uri(String mThumnail_uri) {
        this.mThumnail_uri = mThumnail_uri;
    }

    @Override
    public String getTitle() {
        return mName;
    }
}
