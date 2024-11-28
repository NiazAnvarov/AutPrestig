package com.example.autoprestig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class BronStatusActivity extends AppCompatActivity {

    private int timeInMinutes = 0;
    private Handler handler;
    private Runnable runnable;
    private TextView timerTextView;
    private  TextView probegView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bron_status);
        timerTextView = findViewById(R.id.textViewTime);
        probegView = findViewById(R.id.textProbeg);
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                timeInMinutes++;
                timerTextView.setText(" " + timeInMinutes + " мин.");
                probegView.setText(" " + timeInMinutes/10.0 + " км");
                handler.postDelayed(this, 600); // Обновление каждую минуту
            }
        };
        handler.post(runnable);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Останавливаем таймер при уничтожении activity
    }

    public void onClickFinish(View view) {
        Intent intent = new Intent(this, BronFinishActivity.class);
        intent.putExtra("time_in_minutes", timeInMinutes);
        startActivity(intent);
    }
}