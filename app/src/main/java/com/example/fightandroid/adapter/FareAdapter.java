package com.example.fightandroid.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.listener.FareItemClickListener;
import com.example.fightandroid.model.Fare;
import com.example.fightandroid.model.Flight;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FareAdapter extends RecyclerView.Adapter<FareAdapter.FareViewHolder> {
    private List<Fare> fares;
    private FareItemClickListener fareItemClickListener;

    public FareAdapter(List<Fare> fares,FareItemClickListener fareItemClickListener) {
        this.fares = fares;
        this.fareItemClickListener=fareItemClickListener;
    }

    @NonNull
    @Override
    public FareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fare_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FareViewHolder holder, int position) {
        Fare fare= fares.get(position);
        Flight flight= fare.getFlight();
        holder.tvFlightCode.setText(flight.getCode());
        holder.tvDepartureAirportCode.setText(flight.getDepartureAirport().getCode());
        holder.tvArrivalAirportCode.setText(flight.getArrivalAirport().getCode());
        holder.tvAircraftType.setText(flight.getAircraftType());
        holder.tvDuration.setText(flight.getDuration()+" phút");
        holder.tvPrice.setText(fare.getPrice()+" VND");
        holder.tvAirlineName.setText(flight.getAirline().getName());
        Picasso.get().load(flight.getAirline().getLogoUrl()).into(holder.ivAirline);

        holder.tvDetailFare.setOnClickListener(v -> fareItemClickListener.showDetailFare(fare));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            holder.tvArrivalTime.setText(LocalDateTime.parse(flight.getArrivalDate(), formatter).toLocalTime().toString());
            holder.tvDepartureTime.setText(LocalDateTime.parse(flight.getDepartureDate(), formatter).toLocalTime().toString());
            holder.tvDepartureDate.setText(LocalDateTime.parse(flight.getDepartureDate(), formatter).toLocalDate().toString());
            holder.tvArrivalDate.setText(LocalDateTime.parse(flight.getArrivalDate(), formatter).toLocalDate().toString());




        }


    }

    @Override
    public int getItemCount() {
        if(fares!=null) return fares.size();
        return 0;
    }

    public static class FareViewHolder extends RecyclerView.ViewHolder {
        TextView tvAirlineName,
                tvFlightCode,
                tvAircraftType,
                tvDepartureDate,
                tvArrivalDate,
                tvDepartureTime,
                tvArrivalTime,
                tvDepartureAirportCode,
                tvArrivalAirportCode,
                tvPrice,
                tvDetailFare,
                tvDuration;
        ImageView ivAirline;


        public FareViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlightCode=itemView.findViewById(R.id.tvFlightCodeFare);
            ivAirline=itemView.findViewById(R.id.ivAirlineFare);
            tvAirlineName=itemView.findViewById(R.id.tvAirlineName);
            tvAircraftType=itemView.findViewById(R.id.tvAircraftTypeFare);
            tvDuration=itemView.findViewById(R.id.tvDuration);
            tvDetailFare=itemView.findViewById(R.id.tvDetailFare);
            tvPrice=itemView.findViewById(R.id.tvPriceFare);
            tvArrivalDate=itemView.findViewById(R.id.tvArrivalDateFare);
            tvDepartureDate=itemView.findViewById(R.id.tvDepartureDateFare);
            tvArrivalTime=itemView.findViewById(R.id.tvArrivalTimeFare);
            tvDepartureTime=itemView.findViewById(R.id.tvDepartureTimeFare);
            tvArrivalAirportCode=itemView.findViewById(R.id.tvArrivalAirportFare);
            tvDepartureAirportCode=itemView.findViewById(R.id.tvDepartureAirportFare);
        }
    }
}
