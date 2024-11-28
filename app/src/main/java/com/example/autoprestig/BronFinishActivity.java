package com.example.autoprestig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class BronFinishActivity extends AppCompatActivity {

    private int timeInMinutes = 0;
    private Handler handler;
    private Runnable runnable;
    private TextView timerTextView;
    private  TextView probegView;
    private TextView itogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bron_finish);

        // Получаем значение timeInMinutes из Intent
        Intent intent = getIntent();
        timeInMinutes = intent.getIntExtra("time_in_minutes", 0); // По умолчанию - 0 если значение не передано

        timerTextView = findViewById(R.id.textViewTime);
        probegView = findViewById(R.id.textProbeg);
        itogView = findViewById(R.id.textItog);

        timerTextView.setText(" " + timeInMinutes + " мин.");
        probegView.setText(" " + timeInMinutes / 10.0 + " км");
        itogView.setText("Стоимость поездки: " + timeInMinutes * 10 + " ₽");

    }
    public void onClickPayment(View view) {
        PaymentWindows dialog = PaymentWindows.newInstance();
        dialog.show(getSupportFragmentManager(), "custom");
    }
}