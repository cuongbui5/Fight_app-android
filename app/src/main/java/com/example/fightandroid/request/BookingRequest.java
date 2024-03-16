package com.example.fightandroid.request;


import androidx.annotation.NonNull;

public class BookingRequest {
    private Long fareId;
    private Long contactInfoId;
    private Long contactPassengerId;

    public BookingRequest(Long fareId, Long contactInfoId, Long contactPassengerId) {
        this.fareId = fareId;
        this.contactInfoId = contactInfoId;
        this.contactPassengerId = contactPassengerId;
    }

    @NonNull
    @Override
    public String toString() {
        return "fareId:"+fareId+" contactInfoId:"+contactInfoId+" contactPassengerId:"+contactPassengerId;
    }
}
