package com.theironyard.novauc;

/**
 * Created by JacobP on 3/7/17.
 */
public class Restaurant {

    public int id;
    public String name;
    public String type;
    public String owner;
    public String location;

    public Restaurant(int id, String name,String type, String owner,String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.location = location;

    }

}
