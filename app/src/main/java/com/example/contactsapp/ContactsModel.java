package com.example.contactsapp;

import ir.mirrajabi.searchdialog.core.Searchable;

public class ContactsModel implements Searchable {

    private String mName;
    private String mId;

    private String mUri;
    private String mThumnail_uri;


    public ContactsModel(String mName, String mId, String mUri, String mThumnail_uri) {
        this.mName = mName;
        this.mId = mId;

        this.mUri = mUri;
        this.mThumnail_uri = mThumnail_uri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
