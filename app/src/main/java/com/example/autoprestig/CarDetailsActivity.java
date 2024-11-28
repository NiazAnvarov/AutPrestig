package com.example.autoprestig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CarDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
    }

    public void onClickStart(View view) {
        Intent intent = new Intent(this, BronStatusActivity.class);
        startActivity(intent);
    }
}