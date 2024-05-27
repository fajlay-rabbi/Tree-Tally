package com.example.treetally;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlantDB extends SQLiteOpenHelper {


    public PlantDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "PlantDB.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE plants  ("
                + "ID INT PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "price TEXT,"
                + "type TEXT,"
                + "image BLOB"
                + ")";
        db.execSQL(sql);
        System.out.println("Plant table created!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void onInsert(String name, String price, String type, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO plants (name, price, type, image) VALUES (?, ?, ?, ?)";

        db.execSQL(sql, new String[]{name, price, type, image.toString()});
        db.close();
    }


    public Cursor onFetch() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM plants";


        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }



}
