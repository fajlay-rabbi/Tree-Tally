package com.example.treetally;

public class WishlistModel {
    String id, name, price, type;
    byte[] image;


    public WishlistModel(String id, String name, String price, String type, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
    }
}
