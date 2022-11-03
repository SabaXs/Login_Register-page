package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPage extends AppCompatActivity {

    BottomNavigationView navigationView;
    Button lgotBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        fAuth = FirebaseAuth.getInstance();

        navigationView = findViewById(R.id.btmNavg);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        break;
                    case R.id.insight:
                        startActivity(new Intent(getApplicationContext(), Insights.class));
                        break;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddNew.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        lgotBtn = findViewById(R.id.lgBtn);

        lgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
                fAuth.signOut();
            }
        });

    }
}