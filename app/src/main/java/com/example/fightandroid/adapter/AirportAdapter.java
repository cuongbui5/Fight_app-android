package com.example.fightandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.listener.AirportItemClickListener;
import com.example.fightandroid.model.Airport;

import java.util.List;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.AirportViewHolder> {
    private List<Airport> airports;
    private AirportItemClickListener airportItemClickListener;

    public AirportAdapter(List<Airport> airports,AirportItemClickListener airportItemClickListener) {
        this.airports = airports;
        this.airportItemClickListener=airportItemClickListener;
    }

    @NonNull
    @Override
    public AirportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AirportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.airport_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AirportViewHolder holder, int position) {
        Airport airport=airports.get(position);
        holder.tvCity.setText(airport.getCity()+", "+airport.getCountry());
        holder.tvName.setText(airport.getCode()+" - "+airport.getName());
        holder.cvAirportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                airportItemClickListener.setAirport(airport);

            }
        });

    }

    @Override
    public int getItemCount() {
        if(airports==null) return 0;
        return airports.size();
    }

    public static class AirportViewHolder extends RecyclerView.ViewHolder {
        CardView cvAirportItem;
        TextView tvCity,tvName;

        public AirportViewHolder(@NonNull View itemView) {
            super(itemView);
            cvAirportItem=itemView.findViewById(R.id.cvAirportItem);
            tvCity=itemView.findViewById(R.id.tvCity);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }
}
