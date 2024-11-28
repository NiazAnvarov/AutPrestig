package com.example.autoprestig;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.map.CameraPosition; // Для позиции камеры
import com.yandex.runtime.image.ImageProvider;
import com.yandex.mapkit.map.MapObjectTapListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private MapObjectTapListener mapObjectClickListener;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Point> carSharingLocations;
    private List<String> carNames;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = view.findViewById(R.id.mapview);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        /*carNames = new ArrayList<>();
        carNames.add(new String("Polo"));
        carNames.add(new String("Vesta"));
        carNames.add(new String("Solaris"));*/
        carSharingLocations = new ArrayList<>();
        carSharingLocations.add(new Point(55.751244, 37.618423)); // Москва
        carSharingLocations.add(new Point(59.934280, 30.335099)); // Санкт-Петербург
        carSharingLocations.add(new Point(54.737259, 55.967983)); // Уфа
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Запуск MapKit
        MapKitFactory.getInstance().onStart();
    }
    @Override
    public void onStop() {
        super.onStop();

        // Остановка MapKit
        MapKitFactory.getInstance().onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Остановка MapView
        mapView.onStop();
    }
    private void getLastLocation() {
        // Проверяем наличие разрешения на доступ к местоположению
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return; // Выходим из метода, так как разрешение еще не предоставлено
        }
        // Если разрешение уже предоставлено, то получаем местоположение
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Если местоположение успешно получено
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Point userLocation = new Point(latitude, longitude);

                            // Перемещаем камеру карты на текущее местоположение пользователя
                            mapView.getMap().move(new CameraPosition(userLocation, 15, 0, 0));

                            Bitmap customMarker = createCustomUserMarker();
                            ImageProvider imageProvider = ImageProvider.fromBitmap(customMarker);
                            // Создаем метку на карте в позиции пользователя
                            PlacemarkMapObject userMarker = mapView.getMap().getMapObjects().addPlacemark(userLocation, imageProvider);

                            // Добавляем метки для каршеринговых автомобилей
                            addCarSharingMarkers();
                        } else {
                            Toast.makeText(requireContext(), "Не удалось получить местоположение", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    // Этот метод добавляет метки на карту с текстом под иконкой
    public void addCarSharingMarkers() {
        for (Point location : carSharingLocations) {
            Bitmap customMarker = createCustomMarker("Vesta");
            ImageProvider imageProvider = ImageProvider.fromBitmap(customMarker);

            // Создаем метку на карте
            PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(location, imageProvider);

            // Установите обработчик клика на метке
            //placemark.setUserData("CarDetails"); // Устанавливаем идентификатор или данные

            placemark.addTapListener((mapObject, point) -> {
                // Создаем Intent для перехода на новое Activity
                Intent intent = new Intent(requireContext(), CarDetailsActivity.class);
                startActivity(intent); // Запускаем новое Activity
                return false; // Возвращаем false, чтобы клики обрабатывались дальше
            });
        }
    }
    private Bitmap createCustomMarker(String text) {
        // Ширина и высота значка (вы можете изменить эти значения)
        int width = 150; // 150dp
        int height = 150; // 150dp + текстовый блок

        // Создаем пустой Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Рисуем фон (можно сделать прозрачным)
        canvas.drawColor(Color.TRANSPARENT);

        // Рисуем иконку
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.free_transport);
        Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, 100, 100, false);
        canvas.drawBitmap(scaledIcon, (width - scaledIcon.getWidth()) / 2, 0, null);

        // Рисуем текст
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24); // Размер шрифта
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Рисуем текст под иконкой
        canvas.drawText(text, width / 2, scaledIcon.getHeight() + 30, textPaint); // 30 - отступ

        return bitmap;
    }

    private Bitmap createCustomUserMarker() {
        // Ширина и высота значка (вы можете изменить эти значения)
        int width = 150; // 150dp
        int height = 150; // 150dp + текстовый блок

        // Создаем пустой Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Рисуем фон (можно сделать прозрачным)
        canvas.drawColor(Color.TRANSPARENT);

        // Рисуем иконку
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.user_marker);
        Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, 64, 64, false);
        canvas.drawBitmap(scaledIcon, (width - scaledIcon.getWidth()) / 2, 0, null);

        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Если разрешение предоставлено, снова получаем местоположение
                getLastLocation();
            } else {
                // Разрешение было отклонено
                Toast.makeText(requireContext(), "Разрешение на доступ к местоположению отклонено", Toast.LENGTH_LONG).show();
            }
        }
    }
}
