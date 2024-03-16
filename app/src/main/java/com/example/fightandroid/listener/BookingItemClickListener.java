package com.example.fightandroid.listener;

import com.example.fightandroid.model.Booking;

public interface BookingItemClickListener {
    void seeDetailBooking(Booking booking);
    void payment(Long bookingId);
}
