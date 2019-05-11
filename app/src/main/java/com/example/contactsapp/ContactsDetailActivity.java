package com.example.contactsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

public class ContactsDetailActivity extends AppCompatActivity  {

    private TextView contactsName,contactsPhone,mEmail;
    private ImageView contacts_image;
    private CardView imageCardView;

    private final int PERMISSION_REQUEST_CONTACT = 103;
    private String phone;

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
        phone = intent.getStringExtra("PhoneNumber");
        String email = intent.getStringExtra("Email");
        String image_uri = null;
        if(intent.getStringExtra("Image_uri")!=null){
           image_uri  = intent.getStringExtra("Image_uri");
        }



        getSupportActionBar().hide();

        contactsName.setText(name);
        contactsPhone.setText(phone);
        if(email == null){

            mEmail.setText("No email's there !!");
        }
        else {
            mEmail.setText(email);

        }

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

                // Should we show an explanation?
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
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                makeCall();
            }
        }
        else{
            makeCall();
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
                    makeCall();
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

}
