package com.example.autoprestig;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener/*, PasswordDialogWindow.PasswordDialogListener*/ {

    private Button btnClick;
    private EditText Email;
    private TextView ErrorText;
    private TextView ErrorDBText;

    private List<String> emails = new ArrayList<>();
    private int EmCount = 0;

    private static final String EMAIL_PATTERN = "^(?!\\.)(?!\\.)(?!.*\\.\\.)(?!.*\\.{2,})[a-z]+[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+\\.[|a-z]{2,}$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    /*@Override
    public void onPasswordGenerated(String email, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDatabase();
        } catch (IOException e){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_LOGIN, email);
            cv.put(DatabaseHelper.COLUMN_PASSWORD, password);
            db.insert(DatabaseHelper.TABLE_USER, null, cv);
            db.close();
        }

        finish(); // Закрываем текущую активность
    }*/

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btnClick = findViewById(R.id.regButton);
        btnClick.setOnClickListener(this);
        Email = findViewById(R.id.email);
        ErrorText = findViewById(R.id.errorTextView);
        ErrorDBText = findViewById(R.id.inDBTextView);

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
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }



    @Override
    public void onClick(View view) {
        if(view == btnClick){
            EmCount = 0;
            for(String elem : emails){
                if(Email.getText().toString().equals(elem)){
                    EmCount++;
                }
            }
            if(!isValidEmail(Email.getText().toString())){
                Email.setBackgroundResource(R.drawable.edtxt_error_style);
                ErrorText.setVisibility(View.VISIBLE);
                ErrorDBText.setVisibility(View.INVISIBLE);
            } else if (EmCount != 0) {
                Email.setBackgroundResource(R.drawable.edtxt_error_style);
                ErrorText.setVisibility(View.INVISIBLE);
                ErrorDBText.setVisibility(View.VISIBLE);
            } else {
                ErrorText.setVisibility(View.INVISIBLE);
                ErrorDBText.setVisibility(View.INVISIBLE);
                Email.setBackgroundResource(R.drawable.edtxt_style);


                String email = Email.getText().toString();

                PasswordDialogWindow dialog = PasswordDialogWindow.newInstance(email);
                dialog.show(getSupportFragmentManager(), "custom");
            }
        }
    }



}