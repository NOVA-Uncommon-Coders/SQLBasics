package com.theironyard.novauc;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void insertRestaurant(Connection conn, String name, String type, String owner, String location) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurant VALUES (NULL, ?,?,?,?)");
        stmt.setString(1, name);
        stmt.setString(2, type);
        stmt.setString(3, owner);
        stmt.setString(4, location);
        stmt.execute();

    }
    public static void deleteRestaurant(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurant WHERE name = ?");
        stmt.setString(1, name);
        stmt.executeUpdate();

    }
    public static void updateRestaurant(Connection conn,String name, String type, String owner, String location) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE restaurant SET name = ?, type = ?, owner = ?, location = ? WHERE name = ?");
        stmt.setString(1, name);
        stmt.setString(2, type);
        stmt.setString(3, owner);
        stmt.setString(4, location);
        stmt.setString(5, name);
        stmt.executeUpdate();

    }
    public static ArrayList<Restaurant> selectRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> names = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurant");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            String type = results.getString("type");
            String owner = results.getString("owner");
            String location = results.getString("location");
            names.add(new Restaurant(id, name, type, owner, location));
        }
        return names;
    }

    public static void main(String[] args) throws SQLException {
        // write code here

        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurant (id IDENTITY, name VARCHAR,type VARCHAR, owner VARCHAR, location VARCHAR)");

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();

                    m.put("entry", selectRestaurants(conn));

                    return new ModelAndView(m, "restaurant.html");

                }),
                new MustacheTemplateEngine()
        );


        Spark.post("/create-restaurant", ((request, response) -> {
                    String name = request.queryParams("restaurantName");
                    String type = request.queryParams("cuisineType");
                    String owner = request.queryParams("ownerName");
                    String location = request.queryParams("locationName");
                    insertRestaurant(conn, name, type, owner, location);
                    response.redirect("/");
                    return "";

                })
        );
        Spark.post("/edit-restaurant", ((request, response) -> {
                    String name = request.queryParams("restaurantName");
                    String type = request.queryParams("cuisineType");
                    String owner = request.queryParams("ownerName");
                    String location = request.queryParams("locationName");
                    updateRestaurant(conn, name, type, owner, location);
                    response.redirect("/");
                    return "";

                })
        );
        Spark.post("/delete-restaurant", ((request, response) -> {
                    String name = request.queryParams("restaurantName");
                    deleteRestaurant(conn,name);
                    response.redirect("/");
                    return "";

                })
        );


    }

}
