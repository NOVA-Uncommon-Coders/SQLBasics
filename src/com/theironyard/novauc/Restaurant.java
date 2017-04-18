package com.theironyard.novauc;

/**
 * Created by ANVIL_OCTOPUS on 4/13/17.
 */
public class Restaurant {


    String name;
    String location;
    String mealSpeciality;

    public Restaurant( String name, String location, String mealSpeciality) {

        this.name = name;
        this.location = location;
        this.mealSpeciality = mealSpeciality;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMealSpeciality() {
        return mealSpeciality;
    }

    public void setMealSpeciality(String mealSpecialty) {
        this.mealSpeciality = mealSpecialty;
    }


    }


