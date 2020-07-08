package com.example.contacts.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.pojo.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> Contacts = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.firstName.setText(Contacts.get(position).getFirstName());
        holder.priority.setText(String.valueOf(Contacts.get(position).getPriority()));
        holder.lastName.setText(Contacts.get(position).getLastName());
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

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView firstName;
        private TextView priority;
        private TextView lastName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            priority = itemView.findViewById(R.id.priority);
            lastName = itemView.findViewById(R.id.last_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(Contacts.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Contact Contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}