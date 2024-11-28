package com.example.autoprestig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.security.SecureRandom;
import androidx.annotation.NonNull;


public class PasswordDialogWindow extends  DialogFragment {

    private static final String ARG_EMAIL = "email";

    public static PasswordDialogWindow newInstance(String email) {
        PasswordDialogWindow fragment = new PasswordDialogWindow();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String email = getArguments().getString(ARG_EMAIL);
        String password = generatePassword(8); // Сгенерируйте пароль

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Ваш пароль")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Пароль: " + password + "\nEmail: " + email)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Получаем контекст из активности
                    Context context = getActivity();
                    if (context != null) {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        try {
                            dbHelper.createDatabase(); // Создаем базу данных, если она не существует
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        SQLiteDatabase db = dbHelper.openDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.COLUMN_LOGIN, email); // Вставка логина
                        cv.put(DatabaseHelper.COLUMN_PASSWORD, password); // Вставка пароля
                        long newRowId = db.insert(DatabaseHelper.TABLE_USER, null, cv);
                        if (newRowId == -1) {
                            Log.e("DatabaseHelper", "Error inserting row: email = " + email + ", password = " + password);
                        } else {
                            Log.d("DatabaseHelper", "User inserted with id: " + newRowId);
                        } // Добавление пользователя
                        db.close(); // Закрываем базу данных
                        redirectToAuthActivity(); // Переход к активности для авторизации
                    }
                })
                .create();
    }

    // Метод для перехода к активности авторизации
    private void redirectToAuthActivity() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AuthActivity.class); // Замените AuthActivity на название вашей активности
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Если требуется
            startActivity(intent);
        }
    }

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{};:,.<>?/";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
    public String generatePassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Длина пароля должна быть больше 0.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        // Добавление хотя бы по одному символу из каждой категории для безопасности
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Заменяем оставшиеся символы случайными из всех категорий
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Перемешиваем символы для большей случайности
        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}