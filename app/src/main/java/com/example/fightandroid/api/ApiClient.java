package com.example.fightandroid.api;

import com.example.fightandroid.model.Airport;
import com.example.fightandroid.model.Booking;
import com.example.fightandroid.model.ContactInfo;
import com.example.fightandroid.model.Flight;
import com.example.fightandroid.request.BookingRequest;
import com.example.fightandroid.request.CreateContactInfoRequest;
import com.example.fightandroid.request.LoginRequest;
import com.example.fightandroid.request.RegisterRequest;
import com.example.fightandroid.response.ListFareResponse;
import com.example.fightandroid.response.LoginResponse;
import com.example.fightandroid.response.MessageResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClient {
    @POST("auth/register")
    Call<MessageResponse> register(@Body RegisterRequest registerRequest);
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("airports")
    Call<List<Airport>> getAllAirports();
    @GET("airports/{name}")
    Call<List<Airport>> getAirportsByName(@Path("name") String name);
    @GET("flights/arrivalAirportId={arrivalAirportId}&departureAirportId={departureAirportId}&departureDate={departureDate}")
    Call<List<Flight>> getAllFlights(@Path("arrivalAirportId") Long arrivalAirportId,
                                     @Path("departureAirportId") Long departureAirportId,
                                     @Path("departureDate") String departureDate);
    @GET("fares/arrivalAirportId={arrivalAirportId}&departureAirportId={departureAirportId}&departureDate={departureDate}&fareClass={fareClass}&page={page}&size={size}&sort={sort}")
    Call<ListFareResponse> getAllFares(@Path("arrivalAirportId") Long arrivalAirportId,
                                       @Path("departureAirportId") Long departureAirportId,
                                       @Path("departureDate") String departureDate,
                                       @Path("fareClass") String fareClass,
                                       @Path("page") int page,
                                       @Path("size") int size,
                                       @Path("sort") String sort);


    @POST("contactInfos")
    Call<ContactInfo> createContact(@Body CreateContactInfoRequest createContactInfoRequest);

    @GET("contactInfos")
    Call<List<ContactInfo>> getListContactInfo();

    @POST("bookings")
    Call<Booking> booking(@Body BookingRequest bookingRequest);
    @GET("bookings")
    Call<List<Booking>> getListBooking();

    @GET("bookings/payment/{bookingId}")
    Call<Booking> payment(@Path("bookingId") Long bookingId);






}






