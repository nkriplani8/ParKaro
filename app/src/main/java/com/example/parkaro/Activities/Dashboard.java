package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkaro.Models.userData;
import com.example.parkaro.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class  Dashboard extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView place_name, fare, distance;
    private Button book_spot;
    private String uemail, temp;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    CollectionReference userref;
    FusedLocationProviderClient fusedLocationProviderClient;
    double curr_lat, curr_log, dis_lat=0.0, dis_log;

    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //getting user data...
        //Intent get_intent = getIntent();
        //uemail = get_intent.getStringExtra("email");
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        uemail = preferences.getString("email",null);


        //connecting to database...
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userref = fstore.collection("user");

        //Variable for getting current location...
        Context context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        userref.whereEqualTo("useremail", uemail)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Toast.makeText(getApplicationContext(), "hey"+uemail, Toast.LENGTH_SHORT).show();
                for (QueryDocumentSnapshot documentsnapshot : queryDocumentSnapshots) {
                    if (!(queryDocumentSnapshots.isEmpty())) {
                        userData userdata = documentsnapshot.toObject(userData.class);
                        temp = userdata.getVehicle_no();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(bottomSheetDialog.getContext().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Navigation drawer --------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //navigationView.bringToFront();

        toggle = new ActionBarDrawerToggle(Dashboard.this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View headerView = navigationView.getHeaderView(0);

        if (ActivityCompat.checkSelfPermission(Dashboard.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                //Toast.makeText(getApplicationContext(), "hey", Toast.LENGTH_SHORT).show();
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Context context;
                            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            curr_lat = addresses.get(0).getLatitude();
                            curr_log = addresses.get(0).getLongitude();
                            //Toast.makeText(getApplicationContext(), "hey", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final LatLng pavilion = new LatLng(18.534025, 73.829793);
        mMap.addMarker(new MarkerOptions().position(pavilion).title("Pavilion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pavilion, 10));

        LatLng phoenix = new LatLng(18.562316, 73.916734);
        mMap.addMarker(new MarkerOptions().position(phoenix).title("Phoenix"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(phoenix));

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);*/

        //on click listener...
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                //getting user current location...
                dis_log = curr_log - marker.getPosition().longitude;
                dis_lat = Math.sin(curr_lat * Math.PI / 180.0) * Math.sin(marker.getPosition().latitude * Math.PI / 180.0)
                        + Math.cos(curr_lat * Math.PI / 180.0) * Math.cos(marker.getPosition().latitude * Math.PI / 180.0)
                        * Math.cos(dis_log * Math.PI / 180.0);
                dis_lat = Math.acos(dis_lat);
                //Toast.makeText(getApplicationContext(), "hey" + dis_lat , Toast.LENGTH_SHORT).show();
                dis_lat = dis_lat * 180.0 / Math.PI;
                dis_lat = dis_lat * 60 * 1.1515;
                //Toast.makeText(getApplicationContext(), "hey" + dis_lat , Toast.LENGTH_SHORT).show();
                if (dis_lat != 0.0){
                    //bottomsheet initializer...
                    bottomSheetDialog = new BottomSheetDialog(Dashboard.this, R.style.BottomSheetDialogTheme);
                final View bottomsheetview = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.selected_place, (LinearLayout) findViewById(R.id.selectes_places));
                place_name = bottomsheetview.findViewById(R.id.place_name);
                fare = bottomsheetview.findViewById(R.id.fare);
                distance = bottomsheetview.findViewById(R.id.distance);
                book_spot = bottomsheetview.findViewById(R.id.book_spot);
                place_name.setText(marker.getTitle());
                distance.setText(String.valueOf( Math.round(dis_lat) ) +" Miles");
                fare.setText("10$");
                //setting text for button...
                if (temp == null) {
                    book_spot.setText("Register Vehical");
                } else {
                    book_spot.setText("Book");
                }

                //button click listener...
                book_spot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (temp == null) {
                            Intent intent = new Intent(getApplicationContext(), VehicalRegistration.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                            intent.putExtra("marker",marker.getTitle());
                            intent.putExtra("distance",String.valueOf( Math.round(dis_lat) ) +" Miles");
                            intent.putExtra("fare","10$");
                            startActivity(intent);
                        }

                    }
                });
                bottomSheetDialog.setContentView(bottomsheetview);
                bottomSheetDialog.show();
                }
                return false;
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
        return true;
    }
}