package com.theironyard.novauc;

/**
 * Created by dangelojoyce on 3/7/17.
 */
public class Restaurant {

    public int id;
    public String name;
    public String food;
    public Boolean isOpen;

    public Restaurant (int id, String name, String food, Boolean isOpen){
        this.id = id;
        this.name = name;
        this.food = food;
        this.isOpen = isOpen;

    }

}
