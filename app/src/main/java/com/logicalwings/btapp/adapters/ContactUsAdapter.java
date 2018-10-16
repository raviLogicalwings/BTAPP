package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.ContactUs;

import java.util.List;

public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ContactUs> contactUsList;
    private ContactUs contactUs;

    public ContactUsAdapter(Context context, List<ContactUs> contactUsList) {
        this.context = context;
        this.contactUsList = contactUsList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactUsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_us_layout, parent, false);
        ContactUsAdapter.ViewHolder viewHolder = new ContactUsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactUsAdapter.ViewHolder holder, int position) {
        contactUs = contactUsList.get(position);

        holder.textOfficeName.setText(contactUs.getOfficeName());
        holder.textOfficeAddress.setText(contactUs.getAddress());
        holder.textOfficeContact.setText(contactUs.getMobileNos());
        if (contactUs.getEmailId() != null) {
            holder.textOfficeEmail.setText(contactUs.getEmailId().toString());
        }

    }

    @Override
    public int getItemCount() {
        return contactUsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textOfficeName, textOfficeAddress, textOfficeContact, textOfficeEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            textOfficeAddress = itemView.findViewById(R.id.text_office_address);
            textOfficeName = itemView.findViewById(R.id.text_office_name);
            textOfficeContact = itemView.findViewById(R.id.text_office_contact);
            textOfficeEmail = itemView.findViewById(R.id.text_office_email);

        }
    }
}
