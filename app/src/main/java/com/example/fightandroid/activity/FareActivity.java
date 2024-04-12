package com.example.fightandroid.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.adapter.FareAdapter;
import com.example.fightandroid.adapter.FlightAdapter;
import com.example.fightandroid.api.ApiClientInstance;
import com.example.fightandroid.listener.FareItemClickListener;
import com.example.fightandroid.model.Fare;
import com.example.fightandroid.model.Flight;
import com.example.fightandroid.response.ListFareResponse;
import com.example.fightandroid.util.Constant;
import com.example.fightandroid.util.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FareActivity extends BaseActivity implements FareItemClickListener {
    private RecyclerView rcvFare;
    private AppCompatImageButton btnBack;
    private ImageView ivAirlineDetail;
    private TextView tvArrivalAirportCity,tvDepartureAirportCity, tvAirlineNameDetail, tvFlightCodeDetail, tvDepartureAirportFareDetail, tvArrivalAirportFareDetail, tvFareId, tvAirlineCodeDetail, tvDepartureDateFareDetail, tvDepartureTimeFareDetail, tvArrivalTimeFareDetail, tvStopDetail, tvAircraftTypeFareDetail, tvFareClassDetail, tvPriceDetail, btnBooking,tvTitle;
    private int pageCurrent=1;
    private int totalPage=0;
    private Long arrivalAirportId,departureAirportId;

    private String fareClass="",departureDate;
    private final List<Fare> fareList=new ArrayList<>();
    private FareAdapter fareAdapter;




    @Override
    public void setUp() {

    }

    @Override
    public void initEvents() {
        btnBack.setOnClickListener(v -> finish());
        rcvFare.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rcvFare.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!rcvFare.canScrollVertically(1)&& firstVisibleItemPosition + visibleItemCount >= totalItemCount && pageCurrent < totalPage) {
                    loadMoreData();
                }
            }
        });

    }

    private void loadMoreData() {

        pageCurrent=pageCurrent+1;
        getAllFares();

    }

    private void getAllFares() {
        Dialog dialog= Helper.createDialogLoad(this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        ApiClientInstance.getInstance(this).getAllFares(arrivalAirportId,
                departureAirportId,
                departureDate,
                fareClass,
                pageCurrent,
                Constant.SIZE,
                Constant.SORT).enqueue(new Callback<ListFareResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ListFareResponse> call, Response<ListFareResponse> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    ListFareResponse fareResponse=response.body();
                    if(fareResponse.getFares().size()==0){
                        Helper.showDialog("Không có vé bay phù hợp!",FareActivity.this);
                        return;
                    }
                    fareList.addAll(fareResponse.getFares());
                    fareAdapter.notifyDataSetChanged();
                    pageCurrent=fareResponse.getPage();
                    totalPage=fareResponse.getTotalPage();
                }else {
                    Helper.handlerErrorResponse(response,FareActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ListFareResponse> call, Throwable t) {
                Helper.showDialog("Error server!"+t.getMessage(),FareActivity.this);
                Log.d("error",t.getMessage());

            }
        });

    }

    @Override
    public void initComponents() {
        rcvFare=findViewById(R.id.rcvFare);
        btnBack=findViewById(R.id.btnBack);
        tvTitle=findViewById(R.id.tvTitle);
        rcvFare.setLayoutManager(new LinearLayoutManager(this));
        fareAdapter=new FareAdapter(fareList,this);
        rcvFare.setAdapter(fareAdapter);



    }

    @Override
    public void getDataFromIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            Flight flight=null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                flight=intent.getSerializableExtra("flight", Flight.class);
                if(flight!=null){
                    arrivalAirportId=flight.getArrivalAirport().getId();
                    departureAirportId=flight.getDepartureAirport().getId();
                    departureDate=flight.getDepartureDate().substring(0,10);
                    tvTitle.setText(flight.getDepartureAirport().getCode()+" - "+flight.getArrivalAirport().getCode());
                    getAllFares();

                }
            }


            if(flight==null){
                String title=intent.getStringExtra("title");
                arrivalAirportId=intent.getLongExtra("arrivalAirportId",0);
                departureAirportId=intent.getLongExtra("departureAirportId",0);
                departureDate=intent.getStringExtra("departureDate");
                fareClass=intent.getStringExtra("fareClass");
                tvTitle.setText(title);
                getAllFares();
            }
        }

    }

    @Override
    public void loadUser() {

    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_fare;
    }

    private void initViewDialog(View view){
        ivAirlineDetail=view.findViewById(R.id.ivAirlineDetail);
        tvAirlineNameDetail=view.findViewById(R.id.tvAirlineNameDetail);
        tvFlightCodeDetail=view.findViewById(R.id.tvFlightCodeDetail);
        tvDepartureAirportFareDetail=view.findViewById(R.id.tvDepartureAirportFareDetail);
        tvArrivalAirportFareDetail=view.findViewById(R.id.tvArrivalAirportFareDetail);
        tvFareId=view.findViewById(R.id.tvFareId);
        tvAirlineCodeDetail=view.findViewById(R.id.tvAirlineCodeDetail);
        tvDepartureDateFareDetail=view.findViewById(R.id.tvDepartureDateFareDetail);
        tvDepartureTimeFareDetail=view.findViewById(R.id.tvDepartureTimeFareDetail);
        tvArrivalTimeFareDetail=view.findViewById(R.id.tvArrivalTimeFareDetail);
        tvStopDetail=view.findViewById(R.id.tvStopDetail);
        tvAircraftTypeFareDetail=view.findViewById(R.id.tvAircraftTypeFareDetail);
        tvFareClassDetail=view.findViewById(R.id.tvFareClassDetail);
        tvPriceDetail=view.findViewById(R.id.tvPriceDetail);
        tvDepartureAirportCity=view.findViewById(R.id.tvDepartureAirportCity);
        tvArrivalAirportCity=view.findViewById(R.id.tvArrivalAirportCity);
        btnBooking=view.findViewById(R.id.btnBooking);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showDetailFare(Fare fare) {

        openBottomSheetFareDetail(fare);


    }

    @SuppressLint("SetTextI18n")
    private void openBottomSheetFareDetail(Fare fare) {
        View view= getLayoutInflater().inflate(R.layout.bottom_sheet_fare_detail,null);
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        initViewDialog(view);
        BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        Flight flight=fare.getFlight();
        Picasso.get().load(flight.getAirline().getLogoUrl()).into(ivAirlineDetail);
        tvAirlineNameDetail.setText(flight.getAirline().getName());
        tvFlightCodeDetail.setText(flight.getCode());
        tvDepartureAirportFareDetail.setText(flight.getDepartureAirport().getCode());
        tvArrivalAirportFareDetail.setText(flight.getArrivalAirport().getCode());
        tvFareId.setText(fare.getId().toString());
        tvAirlineCodeDetail.setText(flight.getAirline().getCode());
        tvStopDetail.setText(flight.getStops().toString());
        tvAircraftTypeFareDetail.setText(flight.getAircraftType());
        tvFareClassDetail.setText(fare.getFareClass());
        tvPriceDetail.setText(fare.getPrice()+" VND");
        tvDepartureAirportCity.setText(flight.getDepartureAirport().getCity());
        tvArrivalAirportCity.setText(flight.getArrivalAirport().getCity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            tvDepartureDateFareDetail.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalDate().toString());
            tvDepartureTimeFareDetail.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalTime().toString());
            tvArrivalTimeFareDetail.setText(LocalDateTime.parse(flight.getArrivalDate(),formatter).toLocalTime().toString());

        }
        btnBooking.setOnClickListener(v -> {
            Intent intent=new Intent(FareActivity.this,BookingActivity.class);
            intent.putExtra("fare",fare);
            startActivity(intent);

        });


    }
}
