package com.example.bottledispenser;

public class Bottle {
    private String name;
    private String manufacturer;
    private double size;
    private double price;

    public Bottle(){
    }

    public Bottle(String n, String m, double s, double p){
        name = n;
        manufacturer = m;
        size = s;
        price = p;
    }
    public String getName(){
        return name;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public double getPrice(){
        return price;
    }
    public double getSize(){
        return size;
    }
}


