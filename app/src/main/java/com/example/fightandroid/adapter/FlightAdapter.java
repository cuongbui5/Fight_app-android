package com.example.fightandroid.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.listener.FlightItemClickListener;
import com.example.fightandroid.model.Flight;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flights;
    private FlightItemClickListener listener;

    public FlightAdapter(List<Flight> flights,FlightItemClickListener listener) {
        this.flights = flights;
        this.listener=listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight=flights.get(position);
        holder.tvFlightCode.setText(flight.getCode());
        holder.cvFlight.setOnClickListener(v -> {
            listener.findFareByFlight(flight);

        });

        Picasso.get().load(flight.getAirline().getLogoUrl()).into(holder.ivAirline);
        Log.d("urlairline",flight.getAirline().getLogoUrl());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            holder.tvArrivalTime.setText(LocalDateTime.parse(flight.getArrivalDate(), formatter).toLocalTime().toString());
            holder.tvDepartureTime.setText(LocalDateTime.parse(flight.getDepartureDate(), formatter).toLocalTime().toString());
            holder.tvAircraftType.setText(flight.getAircraftType().toString());

        }

    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartureTime,tvArrivalTime,tvFlightCode,tvAircraftType;
        ImageView ivAirline;
        CardView cvFlight;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAircraftType=itemView.findViewById(R.id.tvAirCraftType);
            tvArrivalTime=itemView.findViewById(R.id.tvArrivalTime);
            tvFlightCode=itemView.findViewById(R.id.tvFlightCode);
            tvDepartureTime=itemView.findViewById(R.id.tvDepartureTime);
            ivAirline=itemView.findViewById(R.id.ivAirline);
            cvFlight=itemView.findViewById(R.id.cvFlight);
        }
    }
}
