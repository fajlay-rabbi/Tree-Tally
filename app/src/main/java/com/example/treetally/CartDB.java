package com.example.treetally;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CartDB extends SQLiteOpenHelper {
    public CartDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "CartDB.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE carts  ("
                + "ID TEXT PRIMARY KEY,"
                + "name TEXT,"
                + "price TEXT,"
                + "type TEXT,"
                + "quantity TEXT,"
                + "image BLOB,"
                + "total_price TEXT"
                + ")";
        db.execSQL(sql);
        System.out.println("Cart table created!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void onInsert(String ID, String name, String price, String type, byte[] image, String quantity, String total_price) {

        SQLiteDatabase db = this.getWritableDatabase();



        String sql = "INSERT INTO carts (ID, name, price, type, image, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?,?)";


        try{

//            db.execSQL(sql, new String[]{name, price, type, image.toString(), quantity});
//            System.out.println("Cart inserted!!");

            Object[] carts = {ID, name, price, type, image, quantity, total_price};
            db.execSQL(sql, carts);

            
            System.out.println("Cart inserted!!");

        }catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public void onUpdate(String quantity, String total, String ID){
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE carts SET quantity = ? and total_price = ? WHERE ID = ?";

        try{

            db.execSQL(sql, new String[]{quantity, total, ID});
            System.out.println("CartDBonUPDATE: " + quantity + " " + total + " " + ID);

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onDelete(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM carts WHERE ID = ?";
        db.execSQL(sql, new String[]{ID});

        //db.close();
    }



    public Cursor onFindOne(String ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM carts WHERE ID = ?";

        Cursor cursor = null;

        try {

            cursor = db.rawQuery(sql, new String[]{ID});

            System.out.println("CartDBonFindOne: " + ID);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return cursor;
    }



    public Cursor onFetch() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM carts";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }








}
