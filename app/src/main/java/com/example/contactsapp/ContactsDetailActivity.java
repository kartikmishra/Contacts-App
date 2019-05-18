package com.example.contactsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactsDetailActivity extends AppCompatActivity  {

    private static final String TAG = "ContactsDetailActivity";
    private TextView contactsName,contactsPhone,mEmail;
    private ImageView contacts_image;
    private CardView imageCardView;

    private final int PERMISSION_REQUEST_CONTACT = 103;
    private String phone;
    private String id;
    private String phoneNumber;
    private String email;
    private List<ContactDetail> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);

        Intent intent = getIntent();

        contactsPhone = findViewById(R.id.contacts_detail_phone);
        contactsName = findViewById(R.id.contacts__detail_name);
        mEmail = findViewById(R.id.contacts_detail_email);
        contacts_image = findViewById(R.id.contacts__detail_photo);
        imageCardView = findViewById(R.id.cardView);


        String name = intent.getStringExtra("Name").toUpperCase();
        id = intent.getStringExtra("ID");
        Log.d(TAG, "onCreate: "+id);
        String image_uri = null;
        if(intent.getStringExtra("Image_uri")!=null){
           image_uri  = intent.getStringExtra("Image_uri");
        }



        getSupportActionBar().hide();


          contactsName.setText(name);



        int[] colors = getApplicationContext().getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = colors[new Random().nextInt(colors.length)];
        imageCardView.setCardBackgroundColor(randomAndroidColor);

        if(image_uri!=null){

            RelativeLayout.LayoutParams layoutParams = new
                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contacts_image.setLayoutParams(layoutParams);
            Uri uri = Uri.parse(image_uri);
            Picasso.get().load(uri).into(contacts_image);
        }

        askForCallPermission();

        getContacts(id);

    }

    public void makeCall(){
        contactsPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);
            }
        });
    }


    public void askForCallPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Call access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm to make call");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.CALL_PHONE}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();


                } else {


                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CONTACT);


                }
            }else{

                makeCall();
            }
        }
        else{
            makeCall();
        }
    }

    public List<ContactDetail> getContacts(final String ids){



        new AsyncTask<String, Void, List<ContactDetail>>() {
            @Override
            protected List<ContactDetail> doInBackground(String... strings) {

                List<ContactDetail> list = new ArrayList<>();

                Cursor phoneCursor =  getApplicationContext().getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{ids},null);



                if (phoneCursor!=null && phoneCursor.moveToFirst()){
                    phoneNumber = phoneCursor.getString
                            (phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phoneCursor.close();

                Cursor emailCursor = getApplicationContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{ids}, ContactsContract.CommonDataKinds.Email.DISPLAY_NAME + " ASC");


                if (emailCursor!=null && emailCursor.moveToFirst()){
                    email = emailCursor.getString
                            (emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emailCursor.close();

                list.add(new ContactDetail(phoneNumber,email));

                return list;
            }

            @Override
            protected void onPostExecute(List<ContactDetail> contactDetails) {

                if(contactDetails.size()>0){
                    list.clear();
                    list.addAll(contactDetails);

                    contactsPhone.setText(phoneNumber);
                    if(email==null){
                        mEmail.setText("No email's there");
                    }
                    else {
                        mEmail.setText(email);
                    }


                }
            }
        }.execute();

        return list;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();


                } else {
                    Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

}
