package com.example.parkaro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkaro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUp extends AppCompatActivity {
    private Button signup_btn;
    private EditText uname;
    private EditText uemail;
    private EditText unumber;
    private EditText npassword;
    private EditText cpassword;
    private String user_email;
    private boolean flag;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    CollectionReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup_btn = findViewById(R.id.signup_btn);
        uname = findViewById(R.id.user_name);
        uemail = findViewById(R.id.user_email);
        unumber = findViewById(R.id.user_no);
        npassword = findViewById(R.id.npassword);
        cpassword = findViewById(R.id.cpassword);
        //ulicence_no = findViewById(R.id.licence_no);
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userref = fstore.collection("user");
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = uemail.getText().toString().trim();
                //Checking whether user already exist or not -------
                // uid = auth.getCurrentUser().getUid();
                flag = validateUser();
                userref.whereEqualTo("email",user_email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //Toast.makeText(signup2_activity.this, "in here", Toast.LENGTH_SHORT).show();
                            if(task.getResult().size()>0) {
                                Toast.makeText(SignUp.this, "User already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(signup2_activity.this, "False", Toast.LENGTH_SHORT).show();
                                if (flag) {
                                    // Toast.makeText(signup2_activity.this, "True", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                                    intent.putExtra("userno", unumber.getText().toString());
                                    intent.putExtra("uname", uname.getText().toString());
                                    intent.putExtra("uemail", uemail.getText().toString());
                                    intent.putExtra("upassword", npassword.getText().toString());
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public boolean validateUser() {

        if(TextUtils.isEmpty(uname.getText().toString())) {
            uname.setError("Enter your full name");
            uname.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(uemail.getText().toString())) {
            uemail.setError("Enter valid phone number");
            uemail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(unumber.getText().toString())) {
            unumber.setError("Enter your email address");
            unumber.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(npassword.getText().toString())) {
            npassword.setError("Enter your new password");
            npassword.requestFocus();
            return false;
        }   else if (!npassword.getText().toString().equals(cpassword.getText().toString())) {
            cpassword.setError("Passwords doesn't match");
            cpassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}