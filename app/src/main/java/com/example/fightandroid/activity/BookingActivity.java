package com.example.fightandroid.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightandroid.R;
import com.example.fightandroid.adapter.ContactInfoAdapter;
import com.example.fightandroid.api.ApiClientInstance;
import com.example.fightandroid.listener.ContactInfoItemClickListener;
import com.example.fightandroid.model.Booking;
import com.example.fightandroid.model.ContactInfo;
import com.example.fightandroid.model.Fare;
import com.example.fightandroid.model.Flight;
import com.example.fightandroid.request.BookingRequest;
import com.example.fightandroid.request.CreateContactInfoRequest;
import com.example.fightandroid.util.Constant;
import com.example.fightandroid.util.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends BaseActivity implements ContactInfoItemClickListener {
    private TextView tvTitle,tvDepartureAirportCityBooking,tvArrivalAirportCityBooking,
            tvDepartureDateBooking, tvArrivalDateBooking, tvDepartureTimeBooking,
            tvArrivalTimeBooking,tvAirlineNameBooking, tvFareClassBooking, tvDurationBooking,
            tvContactName,tvContactPhone,tvContactMail,
            tvPassengerName,tvPassengerPhone,tvPassengerMail ,tvPrice;
    private AppCompatImageButton btnBack;
    private ImageButton btnAddContactInfo,btnAddPassenger,btnEditContact,btnEditPassenger;
    private RecyclerView rcvContactInfo;
    private BottomSheetDialog bottomSheetDialogCreateContact,bottomSheetDialogChooseContact;
    private int CODE_SET;
    private ContactInfo contact,passenger;
    private CardView cvAddContactInfo,cvContactInfo,cvAddPassenger,cvPassengerInfo;
    private Button btnActionBooking;
    private ImageView ivAirlineBooking;
    private Fare fare;






    private void booking(BookingRequest bookingRequest) {
        ApiClientInstance.getInstance(this).booking(bookingRequest).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if(response.isSuccessful()){
                    Booking booking=response.body();
                    if(booking!=null){
                        Helper.showDialog("Đặt vé thành công!",BookingActivity.this);
                    }
                }else {
                    Helper.handlerErrorResponse(response,BookingActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Helper.showDialog("Error server!",BookingActivity.this);

            }
        });
    }

    private void openBottomSheetDialogContact() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_contact_info,null);
        bottomSheetDialogChooseContact=new BottomSheetDialog(this);
        bottomSheetDialogChooseContact.setContentView(view);
        bottomSheetDialogChooseContact.show();
        getListContactInfo();
        TextView tvAddContact=view.findViewById(R.id.tvAddContact);
        tvAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDialogCreateContactInfo();
            }
        });
        rcvContactInfo=view.findViewById(R.id.rcvContactInfo);
        rcvContactInfo.setLayoutManager(new LinearLayoutManager(this));
        rcvContactInfo.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        
    }

    private void openBottomSheetDialogCreateContactInfo() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_create_contact_info,null);
        bottomSheetDialogCreateContact=new BottomSheetDialog(this);
        bottomSheetDialogCreateContact.setContentView(view);
        bottomSheetDialogCreateContact.show();
        EditText firstName,lastName,phone,mail;
        firstName=view.findViewById(R.id.editFirstName);
        lastName=view.findViewById(R.id.editLastName);
        phone=view.findViewById(R.id.editPhone);
        mail=view.findViewById(R.id.editEmail);
        Button btnCreate=view.findViewById(R.id.btnCreateContact);
        btnCreate.setOnClickListener(v -> {
            String firstNameStr,lastNameStr,phoneStr,mailStr;
            firstNameStr=firstName.getText().toString().trim();
            lastNameStr=lastName.getText().toString().trim();
            phoneStr=phone.getText().toString().trim();
            mailStr=mail.getText().toString().trim();
            if(firstNameStr.equals("") || lastNameStr.equals("") || phoneStr.equals("") || mailStr.equals("")){
                Helper.showDialog("Chưa nhập đầy đủ thông tin",BookingActivity.this);
                return;
            }

            CreateContactInfoRequest createContactInfoRequest=new CreateContactInfoRequest(firstNameStr,lastNameStr,phoneStr,mailStr);
            createNewContactInfo(createContactInfoRequest);


        });
    }

    private void createNewContactInfo(CreateContactInfoRequest createContactInfoRequest) {
        ApiClientInstance.getInstance(this).createContact(createContactInfoRequest).enqueue(new Callback<ContactInfo>() {
            @Override
            public void onResponse(Call<ContactInfo> call, Response<ContactInfo> response) {
                if(response.isSuccessful()){
                    ContactInfo contactInfo=response.body();
                    if(contactInfo!=null){
                        bottomSheetDialogCreateContact.dismiss();
                        getListContactInfo();
                    }
                }else {
                    Helper.handlerErrorResponse(response,BookingActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ContactInfo> call, Throwable t) {
                Helper.showDialog("Error server!",BookingActivity.this);
            }
        });
    }

    private void getListContactInfo() {
        Dialog dialog= Helper.createDialogLoad(this);
        dialog.show();
        ApiClientInstance.getInstance(this).getListContactInfo().enqueue(new Callback<List<ContactInfo>>() {
            @Override
            public void onResponse(Call<List<ContactInfo>> call, Response<List<ContactInfo>> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    List<ContactInfo> contactInfos=response.body();
                    if(contactInfos!=null&& !contactInfos.isEmpty()){
                        ContactInfoAdapter contactInfoAdapter=new ContactInfoAdapter(contactInfos,BookingActivity.this);
                        rcvContactInfo.setAdapter(contactInfoAdapter);
                    }

                }else {
                    Helper.handlerErrorResponse(response,BookingActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<ContactInfo>> call, Throwable t) {
                Helper.showDialog("Error server!",BookingActivity.this);
            }
        });

    }


    @Override
    public void initComponents() {
        btnBack=findViewById(R.id.btnBack);
        tvTitle=findViewById(R.id.tvTitle);
        tvDepartureAirportCityBooking=findViewById(R.id.tvDepartureAirportCityBooking);
        tvArrivalAirportCityBooking=findViewById(R.id.tvArrivalAirportCityBooking);
        tvDepartureDateBooking=findViewById(R.id.tvDepartureDateBooking);
        tvArrivalDateBooking=findViewById(R.id.tvArrivalDateBooking);
        tvDepartureTimeBooking=findViewById(R.id.tvDepartureTimeBooking);
        tvArrivalTimeBooking=findViewById(R.id.tvArrivalTimeBooking);
        tvFareClassBooking=findViewById(R.id.tvFareClassBooking);
        tvDurationBooking=findViewById(R.id.tvDurationBooking);
        tvAirlineNameBooking=findViewById(R.id.tvAirlineNameBooking);
        btnAddPassenger=findViewById(R.id.btnAddPassenger);
        btnAddContactInfo=findViewById(R.id.btnAddContactInfo);
        cvAddContactInfo=findViewById(R.id.cvAddContactInfo);
        cvContactInfo=findViewById(R.id.cvContactInfo);
        cvAddPassenger=findViewById(R.id.cvAddPassenger);
        cvPassengerInfo=findViewById(R.id.cvPassengerInfo);
        tvContactName=findViewById(R.id.tvContactName);
        tvContactMail=findViewById(R.id.tvContactMail);
        tvContactPhone=findViewById(R.id.tvContactPhone);
        tvPassengerMail=findViewById(R.id.tvPassengerMail);
        tvPassengerPhone=findViewById(R.id.tvPassengerPhone);
        tvPassengerName=findViewById(R.id.tvPassengerName);
        btnEditContact=findViewById(R.id.btnEditContact);
        btnEditPassenger=findViewById(R.id.btnEditPassenger);
        btnActionBooking=findViewById(R.id.btnActionBooking);
        tvPrice=findViewById(R.id.tvPriceBooking);
        tvTitle.setText("Thêm thông tin");
        ivAirlineBooking=findViewById(R.id.ivAirlineBooking);




    }

    @Override
    public void getDataFromIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                fare=intent.getSerializableExtra("fare", Fare.class);
                Flight flight=fare.getFlight();
                tvAirlineNameBooking.setText(flight.getAirline().getName());
                tvDurationBooking.setText(flight.getDuration()+" phút");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                tvDepartureDateBooking.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalDate().toString());
                tvArrivalDateBooking.setText(LocalDateTime.parse(flight.getArrivalDate(),formatter).toLocalDate().toString());
                tvDepartureTimeBooking.setText(LocalDateTime.parse(flight.getDepartureDate(),formatter).toLocalTime().toString());
                tvArrivalTimeBooking.setText(LocalDateTime.parse(flight.getArrivalDate(),formatter).toLocalTime().toString());
                tvFareClassBooking.setText(fare.getFareClass());
                tvArrivalAirportCityBooking.setText(flight.getArrivalAirport().getCity());
                tvDepartureAirportCityBooking.setText(flight.getDepartureAirport().getCity());
                tvPrice.setText("VND "+fare.getPrice());
                Picasso.get().load(flight.getAirline().getLogoUrl()).into(ivAirlineBooking);
            }
        }

    }



    @Override
    public void setUp() {

    }

    @Override
    public void initEvents() {
        btnBack.setOnClickListener(v->finish());

        btnAddContactInfo.setOnClickListener(v -> {
            CODE_SET= Constant.SET_CONTACT_INFO;
            openBottomSheetDialogContact();
        });

        btnAddPassenger.setOnClickListener(v -> {
            CODE_SET= Constant.SET_PASSENGER_INFO;
            openBottomSheetDialogContact();
        });

        btnEditPassenger.setOnClickListener(v -> {
            CODE_SET= Constant.SET_PASSENGER_INFO;
            openBottomSheetDialogContact();
        });

        btnEditContact.setOnClickListener(v -> {
            CODE_SET= Constant.SET_CONTACT_INFO;
            openBottomSheetDialogContact();

        });

        btnActionBooking.setOnClickListener(v -> {
            if(contact==null||passenger==null){
                Helper.showDialog("Không thể đặt vé vì chưa đủ thông tin liên hệ!",BookingActivity.this);
                return;
            }
            BookingRequest bookingRequest=new BookingRequest(fare.getId(),contact.getId(),passenger.getId());
            Log.d("booking",bookingRequest.toString());
            booking(bookingRequest);

        });

    }

    @Override
    public void loadUser() {

    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_booking;
    }


    @Override
    public void setContactInfoOnBooking(ContactInfo contactInfo) {
        if(CODE_SET==Constant.SET_PASSENGER_INFO){
            passenger=contactInfo;
            updatePassengerView();
        }else {
            contact=contactInfo;
            updateContactInfoView();

        }
        bottomSheetDialogChooseContact.dismiss();

    }

    private void updateContactInfoView() {
        if(contact!=null){
            cvAddContactInfo.setVisibility(View.GONE);
            cvContactInfo.setVisibility(View.VISIBLE);
            tvContactName.setText(contact.getFirstName()+" "+contact.getLastName());
            tvContactPhone.setText(contact.getNumberPhone());
            tvContactMail.setText(contact.getEmail());

        }else {
            cvAddContactInfo.setVisibility(View.VISIBLE);
            cvContactInfo.setVisibility(View.GONE);

        }

    }

    private void updatePassengerView() {
        if(passenger!=null){
            cvAddPassenger.setVisibility(View.GONE);
            cvPassengerInfo.setVisibility(View.VISIBLE);
            tvPassengerName.setText(passenger.getFirstName()+" "+passenger.getLastName());
            tvPassengerPhone.setText(passenger.getNumberPhone());
            tvPassengerMail.setText(passenger.getEmail());

        }else {
            cvAddPassenger.setVisibility(View.VISIBLE);
            cvPassengerInfo.setVisibility(View.GONE);

        }
    }


}
