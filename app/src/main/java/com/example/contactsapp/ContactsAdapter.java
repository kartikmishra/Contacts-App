package com.example.contactsapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsAdapterViewHolder> {


    private Context mContext;
    public static List<ContactsModel> mContactList = new ArrayList<>();

    private ListItemClickListener mListItemClickListener;


    public interface ListItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    public ContactsAdapter(Context mContext, List<ContactsModel> mContactModel, ListItemClickListener mListItemClickListener) {
        this.mContext = mContext;
        this.mContactList = mContactModel;
        this.mListItemClickListener = mListItemClickListener;
    }

    @NonNull
    @Override
    public ContactsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.contact_list_recyclerview_item,viewGroup,false);
        return new ContactsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapterViewHolder contactsAdapterViewHolder, int i) {

        if(mContactList.size()>0){


            contactsAdapterViewHolder.contactsName.setText(mContactList.get(i).getmName());

            if(mContactList.get(i).getmThumnail_uri()==null){
                contactsAdapterViewHolder.thumbnail_image.setImageResource(R.drawable.sun);
                String firstLetter = mContactList.get(i).getmName().substring(0,1);
                contactsAdapterViewHolder.thumbnail_text.setText(firstLetter);
            }
            else {
                contactsAdapterViewHolder.thumbnail_text.setVisibility(View.GONE);
                Uri uri = Uri.parse(mContactList.get(i).getmThumnail_uri());
                Picasso.get().load(uri).into(contactsAdapterViewHolder.thumbnail_image);
            }

            contactsAdapterViewHolder.setIsRecyclable(false);

        }

    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class ContactsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactsName;
        private ImageView thumbnail_image;
        private TextView thumbnail_text;
        public ContactsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            contactsName = itemView.findViewById(R.id.contacts_name);
            thumbnail_image = itemView.findViewById(R.id.contact_thumbnailImage);
            thumbnail_text = itemView.findViewById(R.id.contact_thumbnailText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mListItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
