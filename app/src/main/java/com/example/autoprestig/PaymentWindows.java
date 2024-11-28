package com.example.autoprestig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.security.SecureRandom;

public class PaymentWindows extends DialogFragment {

    public static PaymentWindows newInstance() {
        PaymentWindows fragment = new PaymentWindows();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Информация об оплате")
                .setMessage("Оплата успешна!")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Получаем контекст из активности
                    Context context = getActivity();
                    if (context != null) {

                        redirectToHomeActivity(); // Переход к активности для авторизации
                    }
                })
                .create();
    }

    // Метод для перехода к активности авторизации
    private void redirectToHomeActivity() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MainMapActivity.class); // Замените AuthActivity на название вашей активности
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Если требуется
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            // Изменяем фон диалога

        }
    }

}
