package com.example.treetally;

public class PlantModel {
    public String id;
   public byte[] image;
   public String name, price, type;

    public PlantModel(String id, byte[] image, String name, String price, String type) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.type = type;
    }


}
