package com.theironyard.novauc;

public class Restaurant {
    private String restName;
    private String bestDish;
    private int restNumWaiters;

    public Restaurant(String restName, String bestDish, int restNumWaiters){
        this.restName = restName;
        this.bestDish = bestDish;
        this.restNumWaiters = restNumWaiters;
    }

    public Restaurant() {}

    public String getRestName() {
        return restName;
    }

    public String getBestDish() {
        return bestDish;
    }

    public int getRestNumWaiters() {
        return restNumWaiters;
    }
}
