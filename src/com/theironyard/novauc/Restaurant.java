package com.theironyard.novauc;

public class Restaurant {
    private String restName;
    private Boolean restTasty;
    private int restNumWaiters;

    public Restaurant(String restName, Boolean restTasty, int restNumWaiters){
        this.restName = restName;
        this.restTasty = restTasty;
        this.restNumWaiters = restNumWaiters;
    }

    public Restaurant() {}

    public String getRestName() {
        return restName;
    }

    public Boolean getRestTasty() {
        return restTasty;
    }

    public int getRestNumWaiters() {
        return restNumWaiters;
    }
}
