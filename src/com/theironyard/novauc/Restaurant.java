package com.theironyard.novauc;

public class Restaurant {
    private int id;
    private int zip;
    private String street;
    private String city;
    private String state;
    private String name;
    private String type;
    private boolean edit;
    private boolean display;

    //id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER
    public Restaurant(int id, String name, String type, String street, String city, String state, int zip ) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.name = name;
        this.type = type;
        this.id = id;
        this.zip = zip;
        this.edit = false;
        this.display = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
