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
import com.example.parkaro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class VehicalRegistration extends AppCompatActivity {

    private Button register_btn;
    private EditText car_model;
    private EditText car_type;
    private EditText car_no;
    private EditText new_pass;
    private EditText confirm_pass;
    private String user_no, user_name, user_email, licence_no;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private boolean flag;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
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
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        user_no = intent.getStringExtra("userno");
        user_name = intent.getStringExtra("uname");
        user_email = intent.getStringExtra("uemail");
        licence_no = intent.getStringExtra("ulicence");

        //Registration process...
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = validateUser();
                if (flag) {
                    // Toast.makeText(signup2_activity.this, "True", Toast.LENGTH_SHORT).show();
                    userData userdata = new userData();
                    userdata.setVehicle_no(car_no.getText().toString());
                    userdata.setVehicle_model(car_model.getText().toString());
                    userdata.setVehicle_type(car_type.getText().toString());
                    userref.add(userdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Intent intent = new Intent(VehicalRegistration.this, BookingActivity.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VehicalRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
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
        } else if (TextUtils.isEmpty(new_pass.getText().toString())) {
            new_pass.setError("Enter your new password");
            new_pass.requestFocus();
            return false;
        } else if (!new_pass.equals(confirm_pass)) {
            confirm_pass.setError("Passwords doesn't match");
            confirm_pass.requestFocus();
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