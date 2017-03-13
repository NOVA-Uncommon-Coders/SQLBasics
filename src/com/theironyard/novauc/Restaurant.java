package com.theironyard.novauc;

import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jerieshasmith on 3/7/17.
 */
public class Restaurant {
   static int i = 0;

    int id;
    String type;
    String location;
    String cuisine;


    public Restaurant( String type, String location, String cuisine) {
        this.id = i++;
        this.type = type;
        this.location = location;
        this.cuisine = cuisine;

    }

    public static int getI() {
        return i;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getCuisine() {
        return cuisine;
    }


}

