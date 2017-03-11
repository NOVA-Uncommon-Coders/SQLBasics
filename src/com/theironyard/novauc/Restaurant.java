package com.theironyard.novauc;

public class Restaurant {
    private int id;
    private String restName;
    private String bestDish;
    private int restNumWaiters;

    public Restaurant(int id, String restName, String bestDish, int restNumWaiters){
        this.id = id;
        this.restName = restName;
        this.bestDish = bestDish;
        this.restNumWaiters = restNumWaiters;
    }

    public Restaurant() {}

    public int getId() {return id; }

    public String getRestName() {
        return restName;
    }

    public String getBestDish() {return bestDish; }

    public int getRestNumWaiters() {
        return restNumWaiters;
    }
}
