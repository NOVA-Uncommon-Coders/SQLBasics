package com.theironyard.novauc;




import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    public static void insertRestaurant(Connection conn, String name, String location, String mealspeciality) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setString(3, mealspeciality);
        stmt.execute();
    }


    public static void deleteRestaurant(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE name=?");
        stmt.setString(1, name);
        stmt.execute();
    }


    public static void selectRestaurant(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restaurants WHERE name=?");
        stmt.setString(1, name);
        stmt.execute();
    }


    public static void updateRestaurant(Connection conn, String name, String updateLocation, String updateMealSpeciality, String updateName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("Update restaurants SET name = ?, location = ?, spec = ? WHERE name = ?");
        stmt.setString(1, updateName);
        stmt.setString(2, updateLocation);
        stmt.setString(3, updateMealSpeciality);
        stmt.setString(4, name);
        stmt.execute();

    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();


        Spark.init();

        Spark.get("/", (request, response) -> {
                    HashMap m = new HashMap();
                    ArrayList<Restaurant> something = selectRestaurants(conn);
                    m.put("restaurant", something);
                    //call select restaurant method here
            // pass in the arraylist to the model
                    return new ModelAndView(m, "index.html");
                }, new MustacheTemplateEngine()


        );
        Spark.post("/create-restaurant", (request, response) -> {
            String location = request.queryParams("location");
            String mealSpeciality = request.queryParams("mealSpeciality");
            String name = request.queryParams("name");
            insertRestaurant(conn, name, location, mealSpeciality);
            response.redirect("/");
            return "";

        });


        Spark.post("/delete-restaurant", ((request, response) -> {
            String name = request.queryParams("name");
            deleteRestaurant(conn, name);
            response.redirect("/");
            return "";
        }));


        Spark.post("/update-restaurant", (request, response) -> {
            String whichEdit = request.queryParams("whichEdit");
            String location = request.queryParams("editLocation");
            String mealSpeciality = request.queryParams("editMealSpec");
            String name = request.queryParams("editName");

            updateRestaurant(conn, whichEdit, location, mealSpeciality, name);
            response.redirect("/");
            return "";

        });
    }


    public static ArrayList<Restaurant> selectRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
           Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
            while (results.next()) {
                String name = results.getString("name");
                String location = results.getString("location");
                String mealSpeciality = results.getString("spec");
                restaurants.add(new Restaurant(name, location, mealSpeciality));
            }
            return restaurants;
            }
}










        // make table here






