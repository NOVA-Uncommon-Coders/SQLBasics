package com.theironyard.novauc;

public class Restaurant {
    private String restaurantName;
    private Boolean tasty;
    private int numWaiters;

    public Restaurant(String restaurantName, Boolean tasty, int numWaiters){
        this.restaurantName = restaurantName;
        this.tasty = tasty;
        this.numWaiters = numWaiters;
    }

    public Restaurant() {}

    public String getRestaurantName() {
        return restaurantName;
    }

    public Boolean getTasty() {
        return tasty;
    }

    public int getNumWaiters() {
        return numWaiters;
    }
}
