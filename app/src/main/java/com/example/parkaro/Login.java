package com.example.parkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //defining variables...
        login_button = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_eye = findViewById(R.id.password_eye);

        //password eye setup...
        password_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == password_eye) {
                    if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        password_eye.setImageResource(image[1]);
                    } else if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        password_eye.setImageResource(image[0]);
                    }
                }
            }
        });

        //login button on click listener...
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Please enter your phone no.");
                    email.requestFocus();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }else {
                    //check for authenticated user using firestore ---------
                    userref.whereEqualTo("userno", email.getText().toString())
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentsnapshot : queryDocumentSnapshots) {
                                if (!(queryDocumentSnapshots.isEmpty())) {
                                    userData userdata = documentsnapshot.toObject(userData.class);
                                    pass = userdata.getUserpassword();
                                    if (pass.equals(password.getText().toString())) {
                                        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("isUserLogin", true);
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                                        intent.putExtra("userno", email.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid number or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //savePrefData();
                    //finish();
                }
            }
        });

    }
}