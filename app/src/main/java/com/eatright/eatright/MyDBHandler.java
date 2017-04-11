package com.eatright.eatright;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "eatRightDB.db";
    public static int DATABASE_VERSION = 2;
    public static String TABLE_USERPREFERENCES = "userPreferences";
    public static String COLUMN_USERNAME = "_userName";
    public static String COLUMN_MEALTYPE = "_mealtype";
    public static String COLUMN_AVOIDVEG = "_avoidVeg";
    public static String COLUMN_AVOIDMEAT = "_avoidMeat";
    public static String COLUMN_CONDITIONS = "_conditions";
    public static String COLUMN_DIETS = "_diets";
    public static String COLUMN_CALORIES = "_calories";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USERPREFERENCES + " ( " +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_MEALTYPE + " TEXT, " +
                COLUMN_AVOIDVEG + " TEXT, " +
                COLUMN_AVOIDMEAT + " TEXT, " +
                COLUMN_CONDITIONS + " TEXT, " +
                COLUMN_DIETS + " TEXT, " +
                COLUMN_CALORIES + " TEXT " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERPREFERENCES);
        onCreate(db);
    }

    public int addPreferences(UserPreferences userPreferences) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userPreferences.get_username());
        values.put(COLUMN_MEALTYPE, userPreferences.get_mealtype());
        values.put(COLUMN_AVOIDMEAT, userPreferences.get_avoidMeat());
        values.put(COLUMN_AVOIDVEG, userPreferences.get_avoidVeg());
        values.put(COLUMN_CONDITIONS, userPreferences.get_conditions());
        values.put(COLUMN_DIETS, userPreferences.get_diets());
        values.put(COLUMN_CALORIES, userPreferences.get_calories());

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT count(*) FROM " + TABLE_USERPREFERENCES;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int recordCount = cursor.getInt(0);
        if (recordCount > 0) {
            db.delete(TABLE_USERPREFERENCES, null, null);
            db.insert(TABLE_USERPREFERENCES, null, values);
            recordCount = 555;
        } else {
            db.insert(TABLE_USERPREFERENCES, null, values);
            recordCount = 666;
        }
        db.close();
        cursor.close();

        return recordCount;
    }

    public ArrayList<String> retrievePreferences() {
        SQLiteDatabase db = getWritableDatabase();
        String query1 = "SELECT count(*) FROM " + TABLE_USERPREFERENCES;
        Cursor cursor = db.rawQuery(query1, null);
        cursor.moveToFirst();
        int recordCount = cursor.getInt(0);
        String query2 = "SELECT * FROM " + TABLE_USERPREFERENCES;
        Cursor cursor2 = db.rawQuery(query2, null);
        cursor2.moveToFirst();
        ArrayList<String> allRecords = new ArrayList<String>();
        if (recordCount > 0) {
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_USERNAME)) != null || (cursor2.getString(cursor2.getColumnIndex(COLUMN_USERNAME)).isEmpty())) {
                String col_username = cursor2.getString(cursor2.getColumnIndex(COLUMN_USERNAME));
                allRecords.add(col_username);
            }
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_MEALTYPE)) != null || (cursor2.getString(cursor2.getColumnIndex(COLUMN_MEALTYPE)).isEmpty())) {
                String col_mealtype = cursor2.getString(cursor2.getColumnIndex(COLUMN_MEALTYPE));
                allRecords.add(col_mealtype);
            }
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDVEG)) != null || (cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDVEG)).isEmpty())) {
                String col_avoidVeg = cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDVEG));
                allRecords.add(col_avoidVeg);
            }
            if (!cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDMEAT)).isEmpty() || (cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDMEAT)) != null)) {
                String col_avoidMeat = cursor2.getString(cursor2.getColumnIndex(COLUMN_AVOIDMEAT));
                allRecords.add(col_avoidMeat);
            }
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_CONDITIONS)) != null || (!cursor2.getString(cursor2.getColumnIndex(COLUMN_CONDITIONS)).isEmpty())) {
                String col_conditions = cursor2.getString(cursor2.getColumnIndex(COLUMN_CONDITIONS));
                allRecords.add(col_conditions);
            }
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_DIETS)) != null || (!cursor2.getString(cursor2.getColumnIndex(COLUMN_DIETS)).isEmpty())) {
                String col_diets = cursor2.getString(cursor2.getColumnIndex(COLUMN_DIETS));
                allRecords.add(col_diets);
            }
            if (cursor2.getString(cursor2.getColumnIndex(COLUMN_CALORIES)) != null || (!cursor2.getString(cursor2.getColumnIndex(COLUMN_CALORIES)).isEmpty())) {
                String col_calories = cursor2.getString(cursor2.getColumnIndex(COLUMN_CALORIES));
                allRecords.add(col_calories);
            }
        }
        return allRecords;
    }
}

