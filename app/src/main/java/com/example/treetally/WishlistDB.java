package com.example.treetally;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WishlistDB extends SQLiteOpenHelper {

    public WishlistDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "WishlistDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE wishlists  ("
                + "ID INT PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "price TEXT,"
                + "type TEXT,"
                + "image BLOB"
                + ")";
        db.execSQL(sql);
        System.out.println("Wishlist table created!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void onInsert(String name, String price, String type, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO wishlists (name, price, type, image) VALUES (?, ?, ?, ?)";

        db.execSQL(sql, new String[]{name, price, type, image.toString()});
        db.close();
    }

    public void onDelete(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM wishlists WHERE ID = ?";
        db.execSQL(sql, new String[]{ID});
        db.close();
    }

    public Cursor onFetch() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM wishlists";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


}
