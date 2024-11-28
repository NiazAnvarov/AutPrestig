package com.example.autoprestig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMapActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.map){
                    selectedFragment = new HomeFragment();

                } else if (item.getItemId() == R.id.history) {
                    selectedFragment = new HistoryFragment();
                } else if (item.getItemId() == R.id.account) {
                    selectedFragment = new AccountFragment();
                }

                // Заменяем фрагмент
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }
                return true;
            }
        });
        // Загружаем первый фрагмент при запуске
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.map); // выбираем "карты" по умолчанию
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
