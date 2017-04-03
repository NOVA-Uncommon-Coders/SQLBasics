package com.theironyard.novauc;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();

    //static HashMap<String, Restaurant> restaurantHM = new HashMap<>();

    public static void createTables(Connection conn) throws SQLException{
        Statement s = conn.createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS restaurants(id IDENTITY, name VARCHAR, food VARCHAR, isOpen BOOLEAN)");
    }

    public static void insertRestaurant(Connection conn,  String name, String food, boolean isOpen) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, food);
        stmt.setBoolean(3, isOpen);
        stmt.execute();
    }
    public static void deleteRestaurant(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE id=?");
        stmt.setInt(1, id);
        stmt.execute();
    }
    public static ArrayList<Restaurant> selectRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            String food = results.getString("food");
            boolean isOpen = results.getBoolean("isOpen");
            items.add(new Restaurant(id, name, food, isOpen));
        }
        return items;
    }

    public static void updateRestaurant(Connection conn, int id, String name, String  food, boolean isOpen) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE restaurants set name =?, food =?, isOpen =? WHERE id = ?");
        ArrayList<Restaurant> restaurants = selectRestaurants(conn);
        stmt.setString(1, name);
        stmt.setString(2, food);
        stmt.setBoolean(3, isOpen);
        stmt.setInt(4, id);
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {

        //Server.createWebServer().start();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Spark.init();
        createTables(conn);
        Spark.get("/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("restaurants", selectRestaurants(conn));
                    return new ModelAndView(m, "index.html");
                }), new MustacheTemplateEngine()
        );


        Spark.post("/create-restaurant",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String food = request.queryParams("food");
                    boolean isOpen = Boolean.valueOf(request.queryParams("isOpen"));
                    insertRestaurant(conn, name, food, isOpen);
                    response.redirect("/");
                    return "";
                })
        );


        Spark.post("/delete-restaurant",
                (request, response) -> {
            deleteRestaurant(conn, Integer.valueOf(request.queryParams("id")));
            response.redirect("/");
                    return "";
                });


        Spark.post("/edit-restaurant",
                (request, response) -> {
                    updateRestaurant(conn,
                            Integer.valueOf(request.queryParams("id")),
                            request.queryParams("name"),
                            request.queryParams("food"),
                            Boolean.valueOf(request.queryParams("isOpen"))
                            );
                    response.redirect("/");

                    return "";
                });


    }
}








