package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fName, eMail, passWord, phNum;
    Button regBtn;
    TextView logTxt;
    ProgressBar progressBar;

    FirebaseFirestore fstore;
    String userID;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fName = findViewById(R.id.fullName);
        eMail = findViewById(R.id.eMail);
        passWord = findViewById(R.id.password);
        phNum = findViewById(R.id.phoneNum);
        regBtn = findViewById(R.id.regBtn);
        logTxt = findViewById(R.id.logTxt);
        progressBar = findViewById(R.id.progBar);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        if (fauth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainPage.class));
            finish();
        }

        logTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = eMail.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                final String fullname = fName.getText().toString();
                final String phone = phNum.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    eMail.setError("Email is required");
                }
                if (TextUtils.isEmpty(password)) {
                    passWord.setError("Password is required");
                }
                if (password.length() < 6) {
                    passWord.setError("Password must be more than 6 characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser fuser = fauth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure: Email not sent" + e.getMessage());
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                            userID = fauth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("user").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullname);
                            user.put("eMail", email);
                            user.put("Phone No. ", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "OnSuccess: User profile Created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainPage.class));

                        } else {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }

        });


    }
}