package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkaro.Models.userData;
import com.example.parkaro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {

    private Button verify_btn;
    private String verificationid;
    private String user_no,username,useremail,npassword,code;
    private FirebaseAuth mAuth;
    private boolean flag = false;
    private EditText editText;
    private TextView textView;
    private ProgressBar progressBar;
    private FirebaseFirestore fstore;
    private CollectionReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        if (verificationid == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        fstore = FirebaseFirestore.getInstance();
        userref = fstore.collection("user");
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.text_view);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        user_no = intent.getStringExtra("userno");
        username = intent.getStringExtra("uname");
        useremail = intent.getStringExtra("uemail");
        npassword = intent.getStringExtra("upassword");
        editText = findViewById(R.id.code);

        textView.setText("We have sent a verification code on your phone number +91"+user_no+". Please enter the verification code below ");

        if (user_no != null) {
            sendVerificationCode(user_no);
        }

        verify_btn = findViewById(R.id.verify_btn);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = editText.getText().toString();
                verifycode(code);

                if(flag) {

                    userData userdata = new userData();
                    userdata.setUsername(username);
                    userdata.setUserno(user_no);
                    userdata.setUserpassword(npassword);
                    userdata.setUseremail(useremail);

                    //Creating new user on database -------
                    // String uid = mAuth.getCurrentUser().getUid();

                    userref.add(userdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Intent intent = new Intent(OtpVerification.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("useremail",useremail);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }

    //otp verification process ----------
    private void verifycode (String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signinWithCredential(credential);
    }

    private void signinWithCredential (PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    // pinView.setLineColor(Color.GREEN);
                    flag = true;
                } else {
                    Toast.makeText(OtpVerification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void sendVerificationCode (String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            flag = true;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}