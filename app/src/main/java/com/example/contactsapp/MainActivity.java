package com.example.contactsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.ListItemClickListener{

    private static final String TAG = "MainActivity";

    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private final int PERMISSION_REQUEST_CONTACT = 101;
    private List<ContactsModel> contactList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsRecyclerView = findViewById(R.id.contacts_recyclerView);


        askForContactPermission();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        contactsRecyclerView.setHasFixedSize(true);
        contactsRecyclerView.setLayoutManager(linearLayoutManager);

        contactsAdapter = new ContactsAdapter(this,getContactsList(),this);

        contactsRecyclerView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this,ContactsDetailActivity.class);
        intent.putExtra("Name",getContactsList().get(clickedItemIndex).getmName());
        intent.putExtra("PhoneNumber",getContactsList().get(clickedItemIndex).getmPhoneNumber());
        intent.putExtra("Email",getContactsList().get(clickedItemIndex).getmEmail());
        if(getContactsList().get(clickedItemIndex).getmUri()!=null){
            intent.putExtra("Image_uri",getContactsList().get(clickedItemIndex).getmUri());
        }


        startActivity(intent);

    }


    public List<ContactsModel> getContactsList(){



        new AsyncTask<String, Void, List<ContactsModel>>() {

            @Override
            protected List<ContactsModel> doInBackground(String... strings) {
                List<ContactsModel> list = new ArrayList<>();

                Cursor cursor = getApplicationContext().getContentResolver()
                        .query(ContactsContract.Contacts.CONTENT_URI,null,
                                null,null,
                                ContactsContract.Contacts.DISPLAY_NAME + " ASC");


                cursor.moveToFirst();

                while (cursor.moveToNext()){
                    String id = cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    String photo_uri = cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                    String thumbnail_photo_uri = cursor.getString
                            (cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));


                    Cursor phoneCursor = getApplicationContext().getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},null);

                    String phoneNumber = null;

                    while (phoneCursor.moveToNext()){
                        phoneNumber = phoneCursor.getString
                                (phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();

                    Cursor emailCursor = getApplicationContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, ContactsContract.CommonDataKinds.Email.DISPLAY_NAME + " ASC");

                    String email = null;

                    while (emailCursor.moveToNext()){
                        email = emailCursor.getString
                                (emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }


                    list.add(new ContactsModel(name,phoneNumber,email,photo_uri,thumbnail_photo_uri));
                }

                return list;
            }

            @Override
            protected void onPostExecute(List<ContactsModel> contactsModels) {

                if(contactsModels.size()>0)
                {

                    contactList.clear();
                    progressBar.setVisibility(View.INVISIBLE);
                    contactList.addAll(contactsModels);

                    contactsAdapter.notifyDataSetChanged();
                }

            }
        }.execute();

        return contactList;
    }


    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                getContactsList();
            }
        }
        else{
            getContactsList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactsList();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContactsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {

            doSearch();

        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearch() {


        new SimpleSearchDialogCompat(MainActivity.this, "Search....",
                "Search a Contact here....", null, (ArrayList)
                ContactsAdapter.mContactList,new SearchResultListener<ContactsModel>(){
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, ContactsModel contactsModel, int i) {

                int p = ContactsAdapter.mContactList.indexOf(contactsModel);

                contactsRecyclerView.scrollToPosition(p);
                baseSearchDialogCompat.dismiss();
            }
        }).show();

        contactsRecyclerView.smoothScrollToPosition(contactsAdapter.getItemCount());

    }
}
