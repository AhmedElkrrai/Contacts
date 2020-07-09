package com.example.contacts.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.pojo.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> Contacts = new ArrayList<>();
    private OnItemClickListener listener;


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        holder.firstName.setText(Contacts.get(position).getFirstName());
        holder.lastName.setText(Contacts.get(position).getLastName());
        holder.phoneNumber.setText(Contacts.get(position).getPhoneNumber());

    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView phoneNumber;
        public TextView firstName;
        public TextView lastName;
        public ImageView callButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            callButton = itemView.findViewById(R.id.call);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION)
                            listener.onItemClicked(Contacts.get(position));
                    }
                }
            });

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION)
                            listener.onCallClicked(Contacts.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Contact Contact);
        void onCallClicked(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return Contacts.size();
    }

    public void setList(List<Contact> allContacts) {
        this.Contacts = allContacts;
        notifyDataSetChanged();
    }

    public Contact getContactAt(int position) {
        return Contacts.get(position);
    }

}