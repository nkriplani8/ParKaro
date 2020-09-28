package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkaro.QrCodeActivity;
import com.example.parkaro.R;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView place, fare, date, distance;
    private EditText stime, etime;
    int t1hrs, t1min, t2hrs, t2min, cthrs,ctmin;
    private Boolean flag = true;
    int cal_time;
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
        fare = findViewById(R.id.show_fare);
        distance = findViewById(R.id.show_distance);
        stime = (EditText) findViewById(R.id.show_strat_time);
        etime = (EditText) findViewById(R.id.show_end_time);
        date = findViewById(R.id.show_date);
        confirm_btn = findViewById(R.id.confirm_booking);
        //stime_btn = findViewById(R.id.start_time);
        //etime_btn = findViewById(R.id.end_time);

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
        place.setText(intent.getStringExtra("marker"));
        distance.setText(intent.getStringExtra("distance"));
        fare.setText(intent.getStringExtra("fare"));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(simpleDateFormat.format(calendar.getTime()));
        cthrs = calendar.get(Calendar.HOUR_OF_DAY);
        ctmin = calendar.get(Calendar.MINUTE);
        //Date time = calendar.getTime();
        //Toast.makeText(getApplicationContext(), new String(String.valueOf(time)),Toast.LENGTH_LONG).show();

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
                            t2min = 24;
                        }
                        stime.setText(t1hrs + ":"+ t1min);
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
                            t2min = 24;
                        }
                        etime.setText(t2hrs + ":"+ t2min);
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
                if(TextUtils.isEmpty(stime.getText().toString())) {
                    //etime.setFocusable(true);
                    //stime.setError("Enter your full name");
                    //stime.requestFocus();
                    Toast.makeText(getApplicationContext(),"Please Enter Start Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                else if(TextUtils.isEmpty(etime.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please Enter End Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }else if(cthrs > t1hrs || (cthrs==t1hrs && ctmin > t1min)){
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Start Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }else if(t1hrs > t2hrs ){
                    Toast.makeText(getApplicationContext(),"Please Enter Valid End Time",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                else{
                    flag = true;
                    if(flag){
                    Intent intent = new Intent(getApplicationContext(), QrCodeActivity.class);
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