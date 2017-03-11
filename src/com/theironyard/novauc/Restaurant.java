package com.theironyard.novauc;

/**
 * Created by octavio on 3/7/17.
 */
public class Restaurant {

        public int id;
        public String text;
        public boolean isOpen;
        public double rating;

        public Restaurant (int id, String text, boolean isOpen, double rating) {
            this.id = id;
            this.text = text;
            this.isOpen = isOpen;
            this.rating = rating;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
