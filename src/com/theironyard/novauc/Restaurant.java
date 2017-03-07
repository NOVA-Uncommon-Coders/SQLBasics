package com.theironyard.novauc;

public class Restaurant {
    private int id;
    private int zip;
    //0 = name, 1 = type, 2 = city, 3 = state
    private String[] restaurantInfo = new String[4];

    public Restaurant(int id, int zip, String[] restaurantInfo ){
        this.restaurantInfo = restaurantInfo;
        this.id = id;
        this.zip = zip;
    }
}
