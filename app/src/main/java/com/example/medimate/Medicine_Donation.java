package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Medicine_Donation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine__donation);

        //Initialize and Assign Varible
        BottomNavigationView  bottomNavigationView=findViewById(R.id.bottom_navigation);

        //set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.medicine_1);

        //Perform  ItemSelected listner
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.medicine_1:
                        return true;

                    case R.id.medicine_2:
                        startActivity(new Intent(getApplicationContext(),Medicine_1.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.medicine_3:
                        startActivity(new Intent(getApplicationContext(),Medicine_2.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}