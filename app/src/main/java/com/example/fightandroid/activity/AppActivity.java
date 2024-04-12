package com.example.fightandroid.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.MainActivity;
import com.example.fightandroid.R;
import com.example.fightandroid.adapter.AirportAdapter;
import com.example.fightandroid.adapter.BookingAdapter;
import com.example.fightandroid.api.ApiClientInstance;
import com.example.fightandroid.broadcast.BroadcastReceiverInternet;
import com.example.fightandroid.listener.AirportItemClickListener;
import com.example.fightandroid.listener.BookingItemClickListener;
import com.example.fightandroid.model.Airport;
import com.example.fightandroid.model.Booking;
import com.example.fightandroid.model.Fare;
import com.example.fightandroid.model.Flight;
import com.example.fightandroid.response.LoginResponse;
import com.example.fightandroid.util.Constant;
import com.example.fightandroid.util.Helper;
import com.example.fightandroid.util.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppActivity extends BaseActivity implements AirportItemClickListener, BookingItemClickListener {
    private Button btnPageFlight,btnPageBooking,btnFilter,btnSeeBooking;
    private EditText editDepartureAirport,editArrivalAirport,editDepartureDate,editFareClass;
    private TextView tvFareClass;
    private RecyclerView rcvAirport,rcvListBooking;
    private int SET_AIRPORT;
    private ImageView ivAirlineBookingDetail;

    private String page=Constant.FLIGHT_PAGE;

    private Airport departureAirport,arrivalAirport;
    private String departureDate="";

    private String fareClass;
    private BottomSheetDialog dialogAirport,dialogBookingDetail;
    private TextView tvDepartureAirportCityBooking,tvArrivalAirportCityBooking,
            tvDepartureDateBooking, tvArrivalDateBooking, tvDepartureTimeBooking,
            tvArrivalTimeBooking,tvAirlineNameBooking, tvFareClassBooking, tvDurationBooking,
            tvContactName,tvContactPhone,tvContactMail,
            tvPassengerName,tvPassengerPhone,tvPassengerMail ,tvPrice;





    private void openListBookingDialog() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_booking_list,null);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        rcvListBooking=view.findViewById(R.id.rcvListBooking);
        rcvListBooking.setLayoutManager(new LinearLayoutManager(this));
        Button btnClose=view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v->bottomSheetDialog.dismiss());
        getAllBookings();

    }

    private void getAllBookings() {
        ApiClientInstance.getInstance(this).getListBooking().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if(response.isSuccessful()){
                    List<Booking> bookings=response.body();
                    BookingAdapter bookingAdapter=new BookingAdapter(bookings,AppActivity.this);
                    rcvListBooking.setAdapter(bookingAdapter);
                }else {
                    Helper.handlerErrorResponse(response,AppActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Helper.showDialog("Error server!",AppActivity.this);
            }
        });
    }

    private void openAirportDialog() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_search_airport,null);
        dialogAirport=new BottomSheetDialog(this);
        dialogAirport.setContentView(view);
        dialogAirport.show();
        BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        getAllAirports();
        SearchView svAirport=view.findViewById(R.id.svSearchAirport);
        rcvAirport=view.findViewById(R.id.rcvAirport);
        rcvAirport.setLayoutManager(new LinearLayoutManager(this));
        rcvAirport.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        svAirport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(Objects.equals(newText, "")){
                    getAllAirports();
                }else {
                    getAirportsByName(newText);
                }
                return false;
            }
        });
    }

    private void getAirportsByName(String name) {
        Dialog dialog= Helper.createDialogLoad(this);
        dialog.show();
        ApiClientInstance.getInstance(this).getAirportsByName(name).enqueue(new Callback<List<Airport>>() {
            @Override
            public void onResponse(Call<List<Airport>> call, Response<List<Airport>> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    AirportAdapter adapter=new AirportAdapter(response.body(),AppActivity.this);
                    rcvAirport.setAdapter(adapter);


                }else {
                    Helper.handlerErrorResponse(response,AppActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Airport>> call, Throwable t) {
                Helper.showDialog("Error server!",AppActivity.this);
            }
        });
    }

    private void getAllAirports() {
        Dialog dialog= Helper.createDialogLoad(this);
        dialog.show();
        ApiClientInstance.getInstance(this).getAllAirports().enqueue(new Callback<List<Airport>>() {
            @Override
            public void onResponse(Call<List<Airport>> call, Response<List<Airport>> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    AirportAdapter adapter=new AirportAdapter(response.body(),AppActivity.this);
                    rcvAirport.setAdapter(adapter);


                }else {
                    Helper.handlerErrorResponse(response,AppActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Airport>> call, Throwable t) {
                Helper.showDialog("Error server!",AppActivity.this);
            }
        });
    }

    private void openFareClassDialog() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_fare_class,null);
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        RadioGroup rgFare=view.findViewById(R.id.rgFare);
        Button btnChooseFare=view.findViewById(R.id.btnChooseFare);
        btnChooseFare.setOnClickListener(v -> {
            editFareClass.setText(fareClass);
            bottomSheetDialog.dismiss();

        });
        rgFare.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId==R.id.rbEconomy){
                fareClass="Economy";
            }else if(checkedId==R.id.rbPremiumEconomy){
                fareClass="Premium Economy";
            }else if(checkedId==R.id.rbBusiness){
                fareClass="Business";
            }else {
                fareClass="First Class";
            }



        });


    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_app;
    }

    @Override
    public void loadUser() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        LoginResponse loginResponse = sharedPreferencesManager.getUserData();
        if (loginResponse == null) {
            Intent newIntent = new Intent(AppActivity.this, MainActivity.class);
            startActivity(newIntent);
        }
    }




    private void openDateDialog() {

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        DatePickerDialog dialog=new DatePickerDialog(AppActivity.this, (view, year, month, dayOfMonth) -> {
            int m=month+1;
            String monthStr;
            String dayStr;
            if(m<10){
                monthStr="0"+m;
            }else {
                monthStr = String.valueOf(m);
            }

            if(dayOfMonth<10){
                dayStr="0"+dayOfMonth;
            }else {
                dayStr = String.valueOf(dayOfMonth);
            }
            departureDate=year+"-"+monthStr+"-"+dayStr;
            editDepartureDate.setText(dayOfMonth+" T"+m+" "+year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUp() {
        if(Objects.equals(page, Constant.FLIGHT_PAGE)){
            tvFareClass.setVisibility(View.GONE);
            editFareClass.setVisibility(View.GONE);
            btnPageFlight.setBackgroundResource(R.drawable.btn_page_bg_click);
            btnPageFlight.setTextColor(getResources().getColor(R.color.primary));
            btnPageBooking.setBackgroundResource(R.drawable.btn_page_bg);
            btnPageBooking.setTextColor(getResources().getColor(R.color.white));
            btnFilter.setText("Tra cứu");
        }else {
            btnPageBooking.setBackgroundResource(R.drawable.btn_page_bg_click);
            btnPageBooking.setTextColor(getResources().getColor(R.color.primary));
            btnPageFlight.setBackgroundResource(R.drawable.btn_page_bg);
            btnPageFlight.setTextColor(getResources().getColor(R.color.white));
            tvFareClass.setVisibility(View.VISIBLE);
            editFareClass.setVisibility(View.VISIBLE);
            btnFilter.setText("Tìm kiếm");
        }
    }

    @Override
    public void initEvents() {
        btnPageFlight.setOnClickListener(v->{
            if(!Objects.equals(page,Constant.FLIGHT_PAGE )){
                page=Constant.FLIGHT_PAGE;
                setUp();
            }

        });
        btnPageBooking.setOnClickListener(v->{
            if(!Objects.equals(page, Constant.BOOKING_PAGE)){
                page=Constant.BOOKING_PAGE;
                setUp();
            }

        });
        editDepartureAirport.setOnClickListener(v -> {
            SET_AIRPORT=Constant.SET_DEPARTURE_AIRPORT;
            openAirportDialog();

        });
        editArrivalAirport.setOnClickListener(v->{
            SET_AIRPORT=Constant.SET_ARRIVAL_AIRPORT;
            openAirportDialog();

        });
        editDepartureDate.setOnClickListener(v -> openDateDialog());
        editFareClass.setOnClickListener(v -> {
            openFareClassDialog();

        });
        btnFilter.setOnClickListener(v -> {
            Intent intent;
            if(arrivalAirport==null||departureAirport==null){
                Helper.showDialog("Bạn chưa chọn sân bay!",AppActivity.this);
                return;
            }

            if(Objects.equals(departureDate, "")){
                Helper.showDialog("Bạn chưa chọn ngày đi!",AppActivity.this);
                return;
            }
            if(Objects.equals(page, Constant.FLIGHT_PAGE)){
                intent = new Intent(AppActivity.this, FlightActivity.class);
                intent.putExtra("arrivalAirportId",arrivalAirport.getId());
                intent.putExtra("departureAirportId",departureAirport.getId());
                intent.putExtra("departureDate",departureDate);

            }else {
                intent = new Intent(AppActivity.this, FareActivity.class);
                intent.putExtra("arrivalAirportId",arrivalAirport.getId());
                intent.putExtra("departureAirportId",departureAirport.getId());
                intent.putExtra("departureDate",departureDate);
                intent.putExtra("fareClass",Helper.removeSpace(editFareClass.getText().toString()));
                if(editFareClass.getText().toString().equals("")){
                    Helper.showDialog("Bạn chưa chọn hạng ghế!",AppActivity.this);
                    return;
                }

            }
            intent.putExtra("title",departureAirport.getCode()+" - "+arrivalAirport.getCode());
            startActivity(intent);

        });
        btnSeeBooking.setOnClickListener(v -> openListBookingDialog());

    }

    @Override
    public void initComponents() {
        btnFilter=findViewById(R.id.btnFilter);
        btnPageBooking=findViewById(R.id.btnPageBooking);
        btnPageFlight=findViewById(R.id.btnPageFlight);
        editArrivalAirport=findViewById(R.id.editArrivalAirport);
        editDepartureAirport=findViewById(R.id.editDepartureAirport);
        editDepartureDate=findViewById(R.id.editDepartureDate);
        editFareClass=findViewById(R.id.editFareClass);
        tvFareClass=findViewById(R.id.tvFareClass);
        btnSeeBooking=findViewById(R.id.btnSeeBooking);

    }



    @Override
    public void getDataFromIntent() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setAirport(Airport airport) {
        if(SET_AIRPORT==Constant.SET_ARRIVAL_AIRPORT){
            arrivalAirport=airport;
            editArrivalAirport.setText(airport.getCity()+" ("+airport.getCode()+")");
            dialogAirport.dismiss();
        }else {
            departureAirport=airport;
            editDepartureAirport.setText(airport.getCity()+" ("+airport.getCode()+")");
            dialogAirport.dismiss();

        }

    }

    private void initViewBottomSheetBookingDetail(View view){
        tvDepartureAirportCityBooking=view.findViewById(R.id.tvDepartureAirportCityBooking);
        tvArrivalAirportCityBooking=view.findViewById(R.id.tvArrivalAirportCityBooking);
        tvDepartureDateBooking=view.findViewById(R.id.tvDepartureDateBooking);
        tvArrivalDateBooking=view.findViewById(R.id.tvArrivalDateBooking);
        tvDepartureTimeBooking=view.findViewById(R.id.tvDepartureTimeBooking);
        tvArrivalTimeBooking=view.findViewById(R.id.tvArrivalTimeBooking);
        tvFareClassBooking=view.findViewById(R.id.tvFareClassBooking);
        tvDurationBooking=view.findViewById(R.id.tvDurationBooking);
        tvAirlineNameBooking=view.findViewById(R.id.tvAirlineNameBooking);
        tvContactName=view.findViewById(R.id.tvContactName);
        tvContactMail=view.findViewById(R.id.tvContactMail);
        tvContactPhone=view.findViewById(R.id.tvContactPhone);
        tvPassengerMail=view.findViewById(R.id.tvPassengerMail);
        tvPassengerPhone=view.findViewById(R.id.tvPassengerPhone);
        tvPassengerName=view.findViewById(R.id.tvPassengerName);
        tvPrice=view.findViewById(R.id.tvPriceBookingDetail);
        ivAirlineBookingDetail=view.findViewById(R.id.ivAirlineBookingDetail);
    }

    @Override
    public void seeDetailBooking(Booking booking) {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_booking_detail,null);
        dialogBookingDetail=new BottomSheetDialog(this);
        dialogBookingDetail.setContentView(view);
        dialogBookingDetail.show();
        initViewBottomSheetBookingDetail(view);
        Fare fare=booking.getFare();
        Flight flight=fare.getFlight();
        Picasso.get().load(flight.getAirline().getLogoUrl()).into(ivAirlineBookingDetail);
        tvAirlineNameBooking.setText(flight.getAirline().getName());
        tvDurationBooking.setText(flight.getDuration()+" phút");

        tvFareClassBooking.setText(fare.getFareClass());
        tvArrivalAirportCityBooking.setText(flight.getArrivalAirport().getCity());
        tvDepartureAirportCityBooking.setText(flight.getDepartureAirport().getCity());
        tvPrice.setText("VND "+fare.getPrice());
        tvContactName.setText(booking.getContactInfo().getFirstName()+" "+booking.getContactInfo().getLastName());
        tvContactMail.setText(booking.getContactInfo().getEmail());
        tvContactPhone.setText(booking.getContactInfo().getNumberPhone());
        tvPassengerName.setText(booking.getPassengerInfo().getFirstName()+" "+booking.getPassengerInfo().getLastName());
        tvPassengerMail.setText(booking.getPassengerInfo().getEmail());
        tvPassengerPhone.setText(booking.getPassengerInfo().getNumberPhone());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            tvDepartureDateBooking.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalDate().toString());
            tvArrivalDateBooking.setText(LocalDateTime.parse(flight.getArrivalDate(),formatter).toLocalDate().toString());
            tvDepartureTimeBooking.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalTime().toString());
            tvArrivalTimeBooking.setText(LocalDateTime.parse(flight.getArrivalDate(),formatter).toLocalTime().toString());
        }
        Button btnPay=view.findViewById(R.id.btnActionPay);
        if(booking.isPaymentStatus()){
            btnPay.setText("Đã thanh toán");
            TextView tvBookingCode=view.findViewById(R.id.tvBookingCode);
            LinearLayout codeContainer=view.findViewById(R.id.llBookingCodeContainer);
            codeContainer.setVisibility(View.VISIBLE);
            tvBookingCode.setText(booking.getBookingCode());
        }else {
            btnPay.setOnClickListener(v->payment(booking.getId()));
        }

    }

    @Override
    public void payment(Long bookingId) {
        ApiClientInstance.getInstance(this).payment(bookingId).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if(response.isSuccessful()){
                    Helper.showDialog("Thanh toán thành công. Làm ơn không để lộ booking code!",AppActivity.this);
                    if(dialogBookingDetail!=null&&dialogBookingDetail.isShowing()){
                        dialogBookingDetail.dismiss();
                    }

                    getAllBookings();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {

            }
        });

    }
}
