package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkaro.Models.userData;
import com.example.parkaro.Models.vehicleData;
import com.example.parkaro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VehicalRegistration extends AppCompatActivity {

    private Button register_btn;
    private EditText car_model;
    private EditText car_type;
    private EditText car_no;
    private String uemail;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private boolean flag;
    FirebaseFirestore fstore;
    FirebaseUser auth;
    CollectionReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehical_registration);

        //initialize valriables...
        register_btn = findViewById(R.id.register_btn);
        car_model = findViewById(R.id.car_model);
        car_type = findViewById(R.id.car_type);
        car_no = findViewById(R.id.car_no);
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        uemail = preferences.getString("email",null);

        //Navigation drawer --------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(VehicalRegistration.this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        fstore = FirebaseFirestore.getInstance();
        userref = fstore.collection("user");
        auth = FirebaseAuth.getInstance().getCurrentUser();
        //id = auth.getUid();

        final Intent intent = getIntent();
        final String place_name = intent.getStringExtra("place name");
        final String distance = intent.getStringExtra("distance");
        final String fare = intent.getStringExtra("fare");
        //licence_no = intent.getStringExtra("ulicence");
        //id = intent.getStringExtra("id");

        //Registration process...
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = validateUser();
                if (flag) {

                    userData userData = new userData();
                    String id = userData.getId();

                    final vehicleData vehicledata = new vehicleData();
                    vehicledata.setVehicle_no(car_no.getText().toString());
                    vehicledata.setVehicle_model(car_model.getText().toString());
                    vehicledata.setVehicle_type(car_type.getText().toString());

                    userref.document(uemail.substring(0,uemail.indexOf("@"))).collection("Vehicle Registration").add(vehicledata);

                    /*userref.whereEqualTo("email",uemail)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentsnapshot : queryDocumentSnapshots) {
                                Toast.makeText(getApplicationContext(),"hey",Toast.LENGTH_LONG).show();


                            }
                        }
                    });*/
                    Intent intent1 = new Intent(getApplicationContext(),BookingActivity.class);
                    intent1.putExtra("place name", place_name);
                    intent1.putExtra("distance", distance);
                    intent1.putExtra("fare", fare);
                    startActivity(intent1);
                    Toast.makeText(getApplicationContext(),"iid",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public boolean validateUser() {

        if (TextUtils.isEmpty(car_no.getText().toString())) {
            car_no.setError("Enter your full name");
            car_no.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(car_model.getText().toString())) {
            car_model.setError("Enter valid phone number");
            car_model.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(car_type.getText().toString())) {
            car_type.setError("Enter your email address");
            car_type.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}