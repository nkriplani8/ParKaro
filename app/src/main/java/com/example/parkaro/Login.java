package com.example.parkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {


    private Button login_button;
    private EditText email;
    private EditText password;
    private ImageView password_eye;
    private int image[] = {R.drawable.eye_img, R.drawable.eyeoff_img};
    private TextView forgot_password;
    private TextView textview_signup;
    private FirebaseFirestore fstore;
    private CollectionReference userref;
    private String num, pass, uid;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.login_btn);

    }
}