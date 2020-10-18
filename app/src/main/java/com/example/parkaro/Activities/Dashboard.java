package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkaro.Adapters.CardAdapter;
import com.example.parkaro.Models.cardData;
import com.example.parkaro.Models.userData;
import com.example.parkaro.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class  Dashboard extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView place_name, fare, distance, card_text, place_add, avl_place, hrs, nav_text;
    private Button book_spot;
    private String uemail, temp, id;
    FirebaseFirestore fstore;
    FirebaseUser auth;
    CollectionReference userref;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ViewPager mslider_view;
    private  int mCurrentPage=0;
    private CardAdapter card_adapter;
    double curr_lat, curr_log, dis_lat=0.0, dis_log,marker_lat,marker_lng;
    Boolean flag = false;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetDialog bottomSheetDialog;
    cardData cardData = new cardData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //getting user data...
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        uemail = preferences.getString("email",null);

        //getting carview data..
        mslider_view = (ViewPager) findViewById(R.id.slider_cardview);
        card_adapter = new CardAdapter(this);

        //Card Slider...
        mslider_view.setAdapter(card_adapter);
        mslider_view.addOnPageChangeListener(viewListener);

         //connecting to database...
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance().getCurrentUser();
        userref = fstore.collection("user");

        //Variable for getting current location...
        Context context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        userref.document(uemail.substring(0,uemail.indexOf("@"))).collection("Vehicle Registration").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    flag = Boolean.FALSE;
                }else{
                    flag = Boolean.TRUE;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
        toggle = new ActionBarDrawerToggle(Dashboard.this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View headerView = navigationView.getHeaderView(0);
        nav_text = headerView.findViewById(R.id.nav_text);
        userref.document(uemail.substring(0,uemail.indexOf("@"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                nav_text.setText(document.getString("username"));
            }
        });


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
        //map styling...
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("Dashboard", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Dashboard", "Can't find style. Error: ", e);
        }
        // Add a marker and move the camera
        int height = 120;
        int width = 120;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.parking);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final LatLng pavilion = new LatLng(18.534025, 73.829793);
        mMap.addMarker(new MarkerOptions().position(pavilion).title("Pavilion")).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pavilion, 10));


        marker_lat = 18.534025;
        marker_lng = 73.829793;

        LatLng phoenix = new LatLng(18.562316, 73.916734);
        mMap.addMarker(new MarkerOptions().position(phoenix).title("Phoenix")).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(phoenix));

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
                cardData.setPlaceName(marker.getTitle());
                mslider_view.getAdapter().notifyDataSetChanged();
                marker_lat = marker.getPosition().latitude;
                marker_lng = marker.getPosition().longitude;
                return false;
            }
        });
    }

    public String getAddress(Context ctx, double lat,double lng){
        String fullAdd=null;
        try{
            Geocoder geocoder= new Geocoder(ctx, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(lat,lng,1);
            if(addresses.size()>0){
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);

                // if you want only city or pin code use following code //
           /* String Location = address.getLocality();
            String zip = address.getPostalCode();
            String Country = address.getCountryName(); */
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return fullAdd;
    }

    public void proceedButtonCall(Boolean f, final String hrs_txt, final String fare_txt) {

        //Proceed button coding...
        if(f) {
                //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                //bottomsheet initializer...
                bottomSheetDialog = new BottomSheetDialog(Dashboard.this, R.style.BottomSheetDialogTheme);
                final View bottomsheetview = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.selected_place, (LinearLayout) findViewById(R.id.selectes_places));

                //Initializing variables...
                place_name = bottomsheetview.findViewById(R.id.place_name);
                place_add = bottomsheetview.findViewById(R.id.place_add);
                fare = bottomsheetview.findViewById(R.id.fare);
                distance = bottomsheetview.findViewById(R.id.distance);
                hrs = bottomsheetview.findViewById(R.id.time);
                avl_place = bottomsheetview.findViewById(R.id.avl_places);
                book_spot = bottomsheetview.findViewById(R.id.book_spot);

                hrs.setText(hrs_txt);
                fare.setText(fare_txt);
                //Toast.makeText(getApplicationContext(),card_adapter.setHrs(),Toast.LENGTH_SHORT).show();
                place_add.setText(getAddress(this,marker_lat,marker_lng));
                avl_place.setText((int) (Math.random()*(100-1)+1) +"/100");
                if(cardData.getPlaceName() == null){
                    place_name.setText("Pavilion");
                }else{
                    place_name.setText(cardData.getPlaceName());
                }
                distance.setText(String.valueOf(Math.round(dis_lat)) + " Miles");
                if (flag) {
                    book_spot.setText("Book");
                } else {
                    book_spot.setText("Register Vehical");
                }

                //button click listener...
                book_spot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                            intent.putExtra("place name", cardData.getPlaceName());
                            intent.putExtra("add", place_add.getText().toString());
                            intent.putExtra("hrs", hrs_txt);
                            intent.putExtra("fare", fare_txt);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), VehicalRegistration.class);
                            intent.putExtra("place name", cardData.getPlaceName());
                            intent.putExtra("distance", String.valueOf(Math.round(dis_lat)) + " Miles");
                            intent.putExtra("hrs", hrs_txt);
                            intent.putExtra("fare", fare_txt);
                            startActivity(intent);
                        }

                    }
                });
                bottomSheetDialog.setContentView(bottomsheetview);
                bottomSheetDialog.show();

        }
    }


    public cardData setPlaceName() {
        return cardData;
    }

    //view pager functions
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //addDotsIndicator(position);//calling indicator fun
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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