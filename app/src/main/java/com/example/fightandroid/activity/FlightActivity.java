package com.example.fightandroid.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.adapter.FlightAdapter;
import com.example.fightandroid.api.ApiClientInstance;
import com.example.fightandroid.listener.FlightItemClickListener;
import com.example.fightandroid.model.Flight;
import com.example.fightandroid.util.Helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightActivity extends BaseActivity implements FlightItemClickListener {
    private RecyclerView rcvFlight;
    private AppCompatImageButton btnBack;
    private TextView tvTitle;



    @Override
    public void setUp() {

    }

    private void getAllFlights(Long arrivalAirportId, Long departureAirportId, String departureDate) {
        Dialog dialog= Helper.createDialogLoad(this);
        dialog.show();
        ApiClientInstance.getInstance(this).getAllFlights(arrivalAirportId,departureAirportId,departureDate).enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    List<Flight> flights=response.body();
                    if(flights.size()==0){
                        Helper.showDialog("Không có lịch bay phù hợp!",FlightActivity.this);
                        return;
                    }
                    FlightAdapter flightAdapter=new FlightAdapter(flights,FlightActivity.this);
                    rcvFlight.setAdapter(flightAdapter);
                }else {
                    Helper.handlerErrorResponse(response,FlightActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Helper.showDialog("Error server!"+t.getMessage(),FlightActivity.this);
                Log.d("error",t.getMessage());

            }
        });

    }


    @Override
    public void initComponents() {
        rcvFlight=findViewById(R.id.rcvFlight);
        btnBack=findViewById(R.id.btnBack);
        tvTitle=findViewById(R.id.tvTitle);
        rcvFlight.setLayoutManager(new LinearLayoutManager(this));
        btnBack.setOnClickListener(v -> finish());

    }

    @Override
    public void getDataFromIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            String title=intent.getStringExtra("title");
            Long arrivalAirportId=intent.getLongExtra("arrivalAirportId",0);
            Long departureAirportId=intent.getLongExtra("departureAirportId",0);
            String departureDate=intent.getStringExtra("departureDate");
            tvTitle.setText(title);
            getAllFlights(arrivalAirportId,departureAirportId,departureDate);

        }

    }

    @Override
    public void loadUser() {

    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_flight;
    }

    @Override
    public void findFareByFlight(Flight flight) {
        Intent intent=new Intent(this,FareActivity.class);
        intent.putExtra("flight",flight);
        startActivity(intent);

    }
}
