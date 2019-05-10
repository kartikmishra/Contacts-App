package com.example.contactsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ContactsDetailActivity extends AppCompatActivity {

    private TextView contactsName,contactsPhone,mEmail;
    private ImageView contacts_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);

        Intent intent = getIntent();

        contactsPhone = findViewById(R.id.contacts_detail_phone);
        contactsName = findViewById(R.id.contacts__detail_name);
        mEmail = findViewById(R.id.contacts_detail_email);
        contacts_image = findViewById(R.id.contacts__detail_photo);

        String name = intent.getStringExtra("Name").toUpperCase();
        String phone = intent.getStringExtra("PhoneNumber");
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


        if(image_uri!=null){
            Uri uri = Uri.parse(image_uri);
            Picasso.get().load(uri).into(contacts_image);
        }

    }
}
