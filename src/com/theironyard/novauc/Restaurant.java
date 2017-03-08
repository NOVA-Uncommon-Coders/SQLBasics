package com.theironyard.novauc;

/**
 * Created by souporman on 3/7/17.
 */
public class Restaurant {
    int id;
    String name;
    String rating;
    String signatureDish;
    String healthGrade;

    public Restaurant(String name, String rating, String signatureDish, String healthGrade) {
        this.name = name.toUpperCase();
        this.rating = rating;
        this.signatureDish = signatureDish.toLowerCase();
        this.healthGrade = healthGrade.toUpperCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSignatureDish() {
        return signatureDish;
    }

    public void setSignatureDish(String signatureDish) {
        this.signatureDish = signatureDish;
    }

    public String getHealthGrade() {
        return healthGrade;
    }

    public void setHealthGrade(String healthGrade) {
        this.healthGrade = healthGrade;
    }
}
