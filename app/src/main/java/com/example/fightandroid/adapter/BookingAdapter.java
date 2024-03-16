package com.example.fightandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.listener.BookingItemClickListener;
import com.example.fightandroid.model.Booking;
import com.example.fightandroid.model.Fare;
import com.example.fightandroid.model.Flight;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;
    private BookingItemClickListener bookingItemClickListener;

    public BookingAdapter(List<Booking> bookings,BookingItemClickListener bookingItemClickListener) {
        this.bookings = bookings;
        this.bookingItemClickListener=bookingItemClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking=bookings.get(position);
        Fare fare=booking.getFare();
        Flight flight=fare.getFlight();
        holder.tvPrice.setText(fare.getPrice()+" VND");
        holder.tvBookingId.setText(booking.getId().toString());
        holder.tvArrivalAp.setText(flight.getArrivalAirport().getCode());
        holder.tvDepartAp.setText(flight.getDepartureAirport().getCode());
        if(booking.isPaymentStatus()){
            holder.btnPay.setText("Đã thanh toán");

        }else {
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookingItemClickListener.payment(booking.getId());
                }
            });
        }
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingItemClickListener.seeDetailBooking(booking);
            }
        });


        

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartAp,tvArrivalAp,tvPrice,tvDetail,tvBookingId;
        Button btnPay;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPay=itemView.findViewById(R.id.btnPayment);
            tvDepartAp=itemView.findViewById(R.id.tvDepartureAirportBooking);
            tvArrivalAp=itemView.findViewById(R.id.tvArrivalAirportBooking);
            tvPrice=itemView.findViewById(R.id.tvPriceFareBooking);
            tvDetail=itemView.findViewById(R.id.tvDetailFareBooking);
            tvBookingId=itemView.findViewById(R.id.tvBookingId);
        }
    }
}
