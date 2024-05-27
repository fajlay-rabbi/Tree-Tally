package com.example.treetally;

public class CartModel {
    String id, name, price, type, quantity, total_price;
    byte[] image;


    public CartModel(String id, String name, String price, String type, String quantity, byte[] image, String total_price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
        this.image = image;
        this.total_price = total_price;
    }



}
