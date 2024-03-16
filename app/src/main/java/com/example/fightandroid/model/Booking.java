package com.example.fightandroid.model;

import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private User user;

    private Fare fare;

    private ContactInfo contactInfo;

    private ContactInfo passengerInfo;
    private String bookingDate;
    private String bookingCode;
    private boolean paymentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ContactInfo getPassengerInfo() {
        return passengerInfo;
    }

    public void setPassengerInfo(ContactInfo passengerInfo) {
        this.passengerInfo = passengerInfo;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
