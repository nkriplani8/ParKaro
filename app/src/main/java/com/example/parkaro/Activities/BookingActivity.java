package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkaro.R;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Math.abs;

public class BookingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView place, today_date, sdate,edate, add,reset_date,stime, etime,stime1,etime1,fare,hrs1,hrs2,hrs3,hrs4,hrs5;
    //private EditText ;
    int t1hrs, t1min, t2hrs, t2min, cthrs,ctmin;
    private Boolean flag = true;
    int cal_time,tempd2, tempd1, total_fare;
    String hrs,datefrom,dateto;
    private  Button confirm_btn;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing varibles...
        setContentView(R.layout.activity_booking);
        place = findViewById(R.id.show_place_name);
        add = findViewById(R.id.show_place_add);
        etime1 = findViewById(R.id.show_etime);
        stime1 = findViewById(R.id.show_stime);
        today_date = findViewById(R.id.todays_date);
        reset_date = findViewById(R.id.reset_date);
        stime = findViewById(R.id.show_strat_time);
        etime = findViewById(R.id.show_end_time);
        sdate = findViewById(R.id.show_sdate);
        edate = findViewById(R.id.show_edate);
        confirm_btn = findViewById(R.id.confirm_booking);
        fare = findViewById(R.id.fare);
        hrs1 = findViewById(R.id.hrs1);
        hrs2 = findViewById(R.id.hrs2);
        hrs3 = findViewById(R.id.hrs3);
        hrs4 = findViewById(R.id.hrs4);
        hrs5 = findViewById(R.id.hrs5);

        //Navigation Drawer...
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(BookingActivity.this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        //Getting data from intent...
        Intent intent = getIntent();
        place.setText(intent.getStringExtra("place name"));
        add.setText(intent.getStringExtra("add"));
        fare.setText(intent.getStringExtra("fare"));
        hrs = intent.getStringExtra("hrs");

        //setiing hrs card view...
        if(hrs1.getText().toString().toLowerCase().equals(hrs.toLowerCase())){
            hrs1.setBackground(getDrawable(R.drawable.card_view_shadow1));
        }else if(hrs2.getText().toString().toLowerCase().equals(hrs.toLowerCase())){
            hrs2.setBackground(getDrawable(R.drawable.card_view_shadow1));
        }else if(hrs3.getText().toString().toLowerCase().equals(hrs.toLowerCase())){
            hrs3.setBackground(getDrawable(R.drawable.card_view_shadow1));
        }else if(hrs4.getText().toString().toLowerCase().equals(hrs.toLowerCase())){
            hrs4.setBackground(getDrawable(R.drawable.card_view_shadow1));
        }else if(hrs5.getText().toString().toLowerCase().equals(hrs.toLowerCase())){
            hrs5.setBackground(getDrawable(R.drawable.card_view_shadow1));
        }

        hrs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hrs5.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs2.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs3.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs4.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs1.setBackground(getDrawable(R.drawable.card_view_shadow1));
                fare.setText("20$");
            }
        });
        hrs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hrs1.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs5.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs3.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs4.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs2.setBackground(getDrawable(R.drawable.card_view_shadow1));
                fare.setText("40$");
            }
        });
        hrs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hrs1.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs2.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs5.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs4.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs3.setBackground(getDrawable(R.drawable.card_view_shadow1));
                fare.setText("70$");
            }
        });
        hrs4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hrs1.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs2.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs3.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs5.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs4.setBackground(getDrawable(R.drawable.card_view_shadow1));
                fare.setText("100$");
            }
        });
        hrs5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hrs1.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs2.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs3.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs4.setBackground(getDrawable(R.drawable.card_view_shadow));
                hrs5.setBackground(getDrawable(R.drawable.card_view_shadow1));
                fare.setText("10$/hr");
            }
        });


        //Calender coding...
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE dd MMM yyyy");
        today_date.setText(simpleDateFormat1.format(calendar.getTime()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        sdate.setText(simpleDateFormat.format(calendar.getTime()));
        edate.setText(simpleDateFormat.format(calendar.getTime()));
        cthrs = calendar.get(Calendar.HOUR_OF_DAY);
        ctmin = calendar.get(Calendar.MINUTE);

        //Date Picker...
        reset_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mon = new DateFormatSymbols().getShortMonths()[month];
                        //tempd2 = dayOfMonth;
                        edate.setText(dayOfMonth+" "+mon+" "+year);
                    }
                },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        //Time Picker...
        stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1hrs = hourOfDay;
                        t1min = minute;
                        if(t2hrs == 0 ){
                            t2hrs = 24;
                        }
                        if(t2min == 0 ){
                            t2min = 00;
                        }
                        stime1.setText(t1hrs + ":"+ t1min);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,t1hrs,t1min);
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1hrs,t1min);
                timePickerDialog.show();
                //stime.setHint(time);
            }
        });

        etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t2hrs = hourOfDay;
                        t2min = minute;
                        //time = t2hrs + ":"+ t2min;
                        if(t2hrs == 0 ){
                            t2hrs = 24;
                        }
                        if(t2min == 0 ){
                            t2min = 00;
                        }
                        etime1.setText(t2hrs + ":"+ t2min);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,t1hrs,t1min);
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1hrs,t1min);
                timePickerDialog.show();
                //Toast.makeText(getApplicationContext(),etime.getText(),Toast.LENGTH_LONG).show();
            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Please Enter Start Time",Toast.LENGTH_LONG);
                if(TextUtils.isEmpty(stime1.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please Enter Start Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                else if(TextUtils.isEmpty(etime1.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please Enter End Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }else if(cthrs + 1 < t1hrs){
                    Toast.makeText(getApplicationContext(),"Advance bookoing can be upto 1hr from now",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                else{
                    flag = true;
                    if(flag){
                        datefrom = sdate.getText().toString().substring(2,6) + " " + sdate.getText().toString().substring(0,2) + "/" +t1hrs+":"+t1min;
                        dateto = edate.getText().toString().substring(2,6) + " " + edate.getText().toString().substring(0,2) + "/" +t2hrs+":"+t2min;

                        tempd1 = Integer.parseInt(sdate.getText().toString().substring(0,2));
                        tempd2 = Integer.parseInt(edate.getText().toString().substring(0,2));
                        int total_min = 24 * abs(tempd1-tempd2) * 60;
                        int min1 = t1hrs * 60 + t1min;
                        int min2 = t2hrs * 60 + t2min;
                        total_min = total_min - (min1-min2);

                        if(total_min <= 60){
                            total_fare = 20;
                        }else if(total_min <= 180){
                            total_fare = 50;
                        }else if(total_min <= 300){
                            total_fare = 70;
                        }else if(total_min <= 420){
                            total_fare = 100;
                        }else{
                            total_fare = total_min * 10;
                        }
                        //Toast.makeText(getApplicationContext(),total_min,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),String.valueOf(total_min/60)+" "+total_fare,Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getSharedPreferences("Booking", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("isParkingBooked");
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), QrCodeActivity.class);
                        intent.putExtra("datefrom", datefrom);
                        intent.putExtra("dateto",dateto);
                        intent.putExtra("fare",String.valueOf(total_fare));
                    startActivity(intent);
                    }
                }
                //Intent intent = new Intent(getApplicationContext(), QrCodeActivity.class);
                //startActivity(intent);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                return true;
            case R.id.register_vehicle:
                Intent intent1 = new Intent(getApplicationContext(), VehicalRegistration.class);
                startActivity(intent1);
                return true;
            case R.id.get_code:
                Intent intent2 = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent2);
                return true;
            case R.id.check_time:
                Intent intent3 = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent3);
                return true;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isUserLogin");
                editor.commit();
                finish();
                Intent intent6 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent6);
                return true;
        }
        return false;
    }
}