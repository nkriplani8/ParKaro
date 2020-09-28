package com.example.parkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkaro.Activities.BookingActivity;
import com.example.parkaro.Activities.Dashboard;
import com.example.parkaro.Activities.Login;
import com.example.parkaro.Activities.VehicalRegistration;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Calendar;
import java.util.Map;

public class QrCodeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView qrcode;
    private Button qrcode_btn;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    int t2hrs, t2min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        //initilize variable...
        qrcode = findViewById(R.id.qr_code);
        qrcode_btn = findViewById(R.id.qr_code_btn);

        //navigation drawere...
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(QrCodeActivity.this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        //qrcode coding...
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode("Booking Confirmed", BarcodeFormat.QR_CODE, 244, 244);
            Bitmap.Config config;
            Bitmap bitmap = Bitmap.createBitmap(244, 244, Config.RGB_565);
            for(int x=0;x<244;x++){
                for(int y = 0; y<244;y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        qrcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        QrCodeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int t2hrs = hourOfDay;
                        int t2min = minute;
                        //time = t2hrs + ":"+ t2min;
                        if(t2hrs == 0 ){
                            t2hrs = 24;
                        }
                        if(t2min == 0 ){
                            t2min = 24;
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,t2hrs,t2min);
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t2hrs,t2min);
                timePickerDialog.show();
                Toast.makeText(getApplicationContext(),"Time Period Extended",Toast.LENGTH_SHORT).show();
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
                Intent intent2 = new Intent(getApplicationContext(), QrCodeActivity.class);
                startActivity(intent2);
                return true;
            case R.id.check_time:
                Intent intent3 = new Intent(getApplicationContext(), BookingActivity.class);
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