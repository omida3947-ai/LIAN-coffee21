package com.lian.coffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cafe_lian.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public boolean addTransaction(String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AMOUNT, amount);
        cv.put(COLUMN_TYPE, type);
        long result = db.insert(TABLE_TRANSACTIONS, null, cv);
        return result != -1;
    }

    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + "='INCOME'", null);
        double total = 0;
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + "='EXPENSE'", null);
        double total = 0;
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_ID + " DESC", null);
    }
}
