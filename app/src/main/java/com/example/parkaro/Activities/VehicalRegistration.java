package com.example.parkaro.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parkaro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class VehicalRegistration extends AppCompatActivity {

    private Button register_btn;
    private EditText car_city;
    private EditText car_state;
    private EditText car_no;
    private EditText new_pass;
    private EditText confirm_pass;
    private String user_no,user_name,user_email,licence_no;
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
        car_city = findViewById(R.id.car_city);
        car_state = findViewById(R.id.car_state);
        car_no = findViewById(R.id.car_no);

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
                    Intent intent = new Intent(getApplicationContext(), VehicalRegistration.class);
                    intent.putExtra("userno", user_no);
                    intent.putExtra("uname", user_name);
                    intent.putExtra("uemail", user_email);
                    intent.putExtra("ulicence", licence_no);
                    intent.putExtra("carno", car_no.getText().toString());
                    intent.putExtra("carcity", car_city.getText().toString());
                    intent.putExtra("carstate", car_state.getText().toString());
                    intent.putExtra("userpass", new_pass.getText().toString());
                    startActivity(intent);
                }

            }
        });

    }
    public boolean validateUser() {

        if(TextUtils.isEmpty(car_no.getText().toString())) {
            car_no.setError("Enter your full name");
            car_no.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(car_state.getText().toString())) {
            car_state.setError("Enter valid phone number");
            car_state.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(car_city.getText().toString())) {
            car_city.setError("Enter your email address");
            car_city.requestFocus();
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
}