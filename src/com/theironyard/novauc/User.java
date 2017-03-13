package com.theironyard.novauc;

/**
 * Created by jerieshasmith on 3/7/17.
 */
public class User {
    int id;
    String userName;
    String password;

    public User(int id, String userName,String password){
        this.id = id;
       this.userName = userName;
       this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
