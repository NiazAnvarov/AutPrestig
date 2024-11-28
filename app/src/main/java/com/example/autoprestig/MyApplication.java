package com.example.autoprestig;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class MyApplication extends Application {
    private static final String MAPKIT_API_KEY = "1d73334f-8ac2-4510-9bac-628620878269"; // Ваш API-ключ

    @Override
    public void onCreate() {
        super.onCreate();
        // Инициализация MapKit с вашим API ключом
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
    }
}
