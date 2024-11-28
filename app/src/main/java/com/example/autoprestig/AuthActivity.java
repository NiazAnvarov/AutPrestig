package com.example.autoprestig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> emails = new ArrayList<>();
    private List<String> passwords = new ArrayList<>();
    private int EmCount = 0;
    private int PsCount = 0;
    private Button btnClick;
    private EditText Login, Password;
    private  TextView ErrorText;
    private boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        btnClick = (Button) findViewById(R.id.authButton);
        btnClick.setOnClickListener(this);
        Login = (EditText) findViewById(R.id.login);
        Password = (EditText) findViewById(R.id.password);
        ErrorText = (TextView) findViewById(R.id.errorTextView);

        // Используем try-with-resources для автоматического закрытия DatabaseHelper
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            dbHelper.createDatabase(); // Создание или открытие базы данных
            try (SQLiteDatabase database = dbHelper.openDatabase()) {
                getData(database); // Передаем базу данных в getData()
            } // SQLiteDatabase автоматически закроется здесь
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getData(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT * FROM user", null);
        if (cursor.moveToFirst()) {
            do {
                // Получение индекса для столбца email и проверка его на -1
                int emailIndex = cursor.getColumnIndex("email");
                int passwordIndex = cursor.getColumnIndex("password");

                if (emailIndex == -1) {
                    Log.e("RegistrationActivity", "Столбец 'email' не найден в таблице.");
                    continue;  // Переход к следующей итерации цикла
                }
                if (passwordIndex == -1) {
                    Log.e("RegistrationActivity", "Столбец 'password' не найден в таблице.");
                    continue;  // Переход к следующей итерации цикла
                }

                // Чтение данных из курсора
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                emails.add(email);
                passwords.add(password);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

    @Override
    public void onClick(View view) {
        check = true;
        if(view == btnClick){
            EmCount = 0;
            PsCount = 0;

            if(Login.getText().toString().isEmpty()){
                check = false;
                Login.setBackgroundResource(R.drawable.edtxt_error_style);
            }

            if(Password.getText().toString().isEmpty()){
                check = false;
                Password.setBackgroundResource(R.drawable.edtxt_error_style);
            }
            if(check){
                for (int i = 0; i < emails.size(); i++) {
                    if (Login.getText().toString().equals(emails.get(i))) {
                        EmCount++;
                    }
                    if(Password.getText().toString().equals(passwords.get(i))){
                        PsCount++;
                    }
                }
                if(EmCount == 0){
                    ErrorText.setVisibility(View.VISIBLE);
                    Login.setBackgroundResource(R.drawable.edtxt_error_style);
                }
                else{
                    Login.setBackgroundResource(R.drawable.edtxt_style);
                    ErrorText.setVisibility(View.INVISIBLE);
                }
                if(PsCount == 0){
                    ErrorText.setVisibility(View.VISIBLE);
                    Password.setBackgroundResource(R.drawable.edtxt_error_style);
                }
                else{
                    Password.setBackgroundResource(R.drawable.edtxt_style);
                    ErrorText.setVisibility(View.INVISIBLE);
                }
                if(EmCount > 0 && PsCount > 0) {
                    Intent intent = new Intent(this, MainMapActivity.class);
                    startActivity(intent);
                }
            }
            else{
                ErrorText.setVisibility(View.VISIBLE);
            }
        }
    }

    public void regClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}