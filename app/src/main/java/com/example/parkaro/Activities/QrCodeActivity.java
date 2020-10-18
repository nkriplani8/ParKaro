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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkaro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Calendar;
import java.util.Random;

public class QrCodeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView qrcode;
    private Button qrcode_btn;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    String uemail;
    private TextView uname,uno,vehicle_type,park_slot,datefrom,dateto, parkadd;
    int t2hrs, t2min;
    FirebaseFirestore fstore;
    FirebaseUser auth;
    CollectionReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        //initilize variable...
        qrcode = findViewById(R.id.qr_code);
        qrcode_btn = findViewById(R.id.qr_code_btn);
        uname = findViewById(R.id.user_name);
        uno = findViewById(R.id.user_no);
        datefrom = findViewById(R.id.date_from);
        dateto = findViewById(R.id.date_to);
        vehicle_type = findViewById(R.id.vehicle_type);
        park_slot = findViewById(R.id.parking_slot);
        parkadd = findViewById(R.id.park_add);
        fstore = FirebaseFirestore.getInstance();
        userref = fstore.collection("user");
        auth = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences1 = getSharedPreferences("Booking", Context.MODE_PRIVATE);

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



        //TextViews Coding...
       if(preferences1.contains("isParkingBooked")){
            uname.setText(preferences1.getString("uname",null));
            uno.setText(preferences1.getString("uno",null));
            datefrom.setText(preferences1.getString("datefrom",null));
            parkadd.setText(preferences1.getString("fare",null));
            park_slot.setText(preferences1.getString("park_slot",null));
            dateto.setText(preferences1.getString("dateto",null));
            vehicle_type.setText(preferences1.getString("vtype",null));
        }else {
            Random r = new Random();
            final Intent intent = getIntent();
            datefrom.setText(intent.getStringExtra("datefrom"));
            dateto.setText(intent.getStringExtra("dateto"));
            parkadd.setText(intent.getStringExtra("fare") + "$");
            SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            uemail = preferences.getString("email", null);
            uname.setText(uemail.substring(0, uemail.indexOf("@")));
            vehicle_type.setText("Two Wheeler");
            park_slot.setText("#" + (char) (65 + r.nextInt(3)) + " " + (int) (Math.random() * (100 - 1) + 1));

            userref.document(uemail.substring(0, uemail.indexOf("@"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    uno.setText("+91" + document.getString("userno"));
                }
            });

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isParkingBooked", true);
        editor.putString("uname",uname.getText().toString());
            editor.putString("uno",uno.getText().toString());
            editor.putString("datefrom",datefrom.getText().toString());
            editor.putString("dateto",dateto.getText().toString());
            editor.putString("vtype",vehicle_type.getText().toString());
            editor.putString("park_slot",park_slot.getText().toString());
            editor.putString("fare",parkadd.getText().toString()+"$");
        editor.commit();
        }
        qrcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QrCodeActivity.this, Dashboard.class);
                startActivity(intent);

            }

        });

    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(QrCodeActivity.this, Dashboard.class);
        startActivity(intent);

        super.onBackPressed();
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