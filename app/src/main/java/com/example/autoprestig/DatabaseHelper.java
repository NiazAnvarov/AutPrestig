package com.example.autoprestig;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "autoprest.db";
    public static final String DATABASE_PATH = "/data/data/com.example.autoprestig/databases/";

    private static final int DATABASE_VERSION = 1;

    // Таблица пользователи
    public static final String TABLE_USER = "user"; // Таблица пользователи
    public static final String COLUMN_LOGIN = "email"; // Столбец для логина
    public static final String COLUMN_PASSWORD = "password"; // Столбец для пароля

    // Таблица банковские карты
    public static final String TABLE_BANKCARD = "bankcard"; // Таблица банковские карты
    public static final String COLUMN_BEMAIL = "email"; // Столбец для email
    public static final String COLUMN_BNUMBER = "number"; // Столбец для номера карты
    public static final String COLUMN_BDATE = "date"; // Столбец для даты окончания работы карты
    public static final String COLUMN_BSVV = "svv"; // Столбец для svv
    public static final String COLUMN_BOWNER = "owner"; // Столбец для владелец карты

    // Таблица водительские удостоверения
    public static final String TABLE_DRIVELICENSE = "drivelicense"; // Таблица водительские права
    public static final String COLUMN_DEMAIL = "email"; // Столбец для email
    public static final String COLUMN_DREGION = "region"; // Столбец для региона
    public static final String COLUMN_DDATEISSUE = "date_issue"; // Столбец для дата выдачи
    public static final String COLUMN_DISSUED = "issued"; // Столбец для кем выдана
    public static final String COLUMN_DSERIESNUMBER = "series_number"; // Столбец для серии и номера
    public static final String COLUMN_DCATEGORIES = "categories"; // Столбец для категорий

    //Таблица автомобили
    public static final String TABLE_CARS = "cars"; // Таблица автомобили
    public static final String COLUMN_CCARID = "car_id"; // Столбец для id автомобиля
    public static final String COLUMN_CMARKA = "marka"; // Столбец для марки
    public static final String COLUMN_CMODEL = "model"; // Столбец для модели
    public static final String COLUMN_CNUMBER = "number"; // Столбец для номер
    public static final String COLUMN_CFUEL = "fuel"; // Столбец для уровень топлива
    public static final String COLUMN_CLATITUDE = "latitude"; // Столбец для широты
    public static final String COLUMN_CLONGITUDE = "longitude"; // Столбец для долгота


    //Таблица история поездок
    public static final String TABLE_TRAVELHISTORY = "travel_history"; // Таблица история поездок
    public static final String COLUMN_TTRAVELID = "travel_id"; // Столбец для id поездки
    public static final String COLUMN_TUSER = "user"; // Столбец для пользователь
    public static final String COLUMN_TCAR = "car"; // Столбец для автомобиль
    public static final String COLUMN_TDATA = "data"; // Столбец для даты поездки
    public static final String COLUMN_TTRAVELCOST = "travel_cost"; // Столбец для стоимость поездки



    private final Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + COLUMN_LOGIN + " TEXT NOT NULL UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
        String CREATE_BANKCARD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BANKCARD + "("
                + COLUMN_BEMAIL + " TEXT NOT NULL UNIQUE,"
                + COLUMN_BNUMBER + " TEXT NOT NULL,"
                + COLUMN_BDATE + " TEXT NOT NULL,"
                + COLUMN_BSVV + " INTEGER NOT NULL,"
                + COLUMN_BOWNER + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_BANKCARD_TABLE);
        String CREATE_CARS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CARS + "("
                + COLUMN_CCARID + " INTEGER NOT NULL UNIQUE,"
                + COLUMN_CMARKA + " TEXT,"
                + COLUMN_CMODEL + " TEXT,"
                + COLUMN_CNUMBER + " TEXT,"
                + COLUMN_CFUEL + " INTEGER,"
                + COLUMN_CLATITUDE + " TEXT,"
                + COLUMN_CLONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE);
        String CREATE_DRIVELICENSE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DRIVELICENSE + "("
                + COLUMN_DEMAIL + " TEXT NOT NULL UNIQUE,"
                + COLUMN_DREGION + " TEXT NOT NULL,"
                + COLUMN_DDATEISSUE + " TEXT NOT NULL,"
                + COLUMN_DISSUED + " TEXT NOT NULL,"
                + COLUMN_DSERIESNUMBER + " TEXT NOT NULL UNIQUE,"
                + COLUMN_DCATEGORIES + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_DRIVELICENSE_TABLE);
        String CREATE_TRAVELHISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRAVELHISTORY + "("
                + COLUMN_TTRAVELID + " INTEGER NOT NULL UNIQUE,"
                + COLUMN_TUSER + " TEXT NOT NULL,"
                + COLUMN_TCAR + " INTEGER NOT NULL,"
                + COLUMN_TDATA + " TEXT NOT NULL,"
                + COLUMN_TTRAVELCOST + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_TRAVELHISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Логика обновления базы данных может быть добавлена здесь
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Ошибка копирования базы данных");
            }
        }
    }

    private boolean checkDatabase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    public SQLiteDatabase openDatabase() {
        String path = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }





}