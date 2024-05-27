package com.example.treetally;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class userImgDB extends SQLiteOpenHelper {

    public userImgDB(@Nullable Context context) {
        super(context, "UserImgDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userImg (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onInsert(String email, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO userImg (email, image) VALUES (?, ?)";
        try{
            Object[] userImg = {email, image};

            db.execSQL(sql, userImg);

            System.out.println("User image inserted!!");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor onFetch(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM userImg WHERE email = ?";
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(sql, new String[]{email});
        }catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


    public void onDeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM userImg";
        try{
            db.execSQL(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
