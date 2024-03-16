package com.example.fightandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.listener.ContactInfoItemClickListener;
import com.example.fightandroid.model.ContactInfo;

import java.util.List;

public class ContactInfoAdapter extends RecyclerView.Adapter<ContactInfoAdapter.ContactInfoViewHolder> {

    private List<ContactInfo> contactInfos;
    private ContactInfoItemClickListener contactInfoItemClickListener;

    public ContactInfoAdapter(List<ContactInfo> contactInfos,ContactInfoItemClickListener contactInfoItemClickListener) {
        this.contactInfos = contactInfos;
        this.contactInfoItemClickListener=contactInfoItemClickListener;
    }

    @NonNull
    @Override
    public ContactInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_info_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactInfoViewHolder holder, int position) {
        ContactInfo contactInfo=contactInfos.get(position);
        holder.tvName.setText(contactInfo.getFirstName()+" "+contactInfo.getLastName());
        holder.llContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactInfoItemClickListener.setContactInfoOnBooking(contactInfo);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactInfos.size();
    }

    public static class ContactInfoViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        LinearLayout llContactInfo;


        public ContactInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvNameContact);
            llContactInfo=itemView.findViewById(R.id.llContactInfo);
        }
    }
}
