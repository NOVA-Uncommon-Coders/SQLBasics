package com.theironyard.novauc;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void insertRestaurant(Connection conn, String restaurantText, boolean isOpen, double rating) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?)");

        stmt.setString(1, restaurantText);
        stmt.setBoolean(2, isOpen);
        stmt.setDouble(3, rating);
        stmt.execute();
    }

    public static void deleteRestaurant(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE VALUES (NULL)");
        stmt.execute();

    }

    public static void selectRestaurant(Connection conn, String restaurantText) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restaurants WHERE id = ?");
        stmt.setInt(1, 1);
        ResultSet r = stmt.executeQuery();
        if (r.next()) {
            int id = r.getInt("id");
            String password = r.getString("password");
            return new Restaurant(id, restaurantText, isOpen, rating);
        }
        ;

    public static void editRestaurant(Connection conn, String restaurantText) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE * FROM restaurants WHERE id = ?");
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {

        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:~/main"); //connection object to particular database
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, name VARCHAR, is_open BOOLEAN, rating DOUBLE)");


        Spark.init();
        Spark.post(
                "/insertRestaurant",
                ((request, response) -> {

                    String restaurantName = request.queryParams("restaurantName");
                    Boolean restaurantOpen = Boolean.valueOf(request.queryParams("restaurantOpen"));
                    double restaurantRating = Double.valueOf(request.queryParams("restaurantRating"));
                    insertRestaurant(conn, restaurantName, restaurantOpen, restaurantRating);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/deleteRestaurant",
                ((request, response) ->
                {
                    public static void deleteRestaurant (Connection conn,int id) throws SQLException {
                    PreparedStatement stmt = conn.prepareStatement("DELETE restaurant SET is_Open = False is_Open WHERE id = ?");
                    stmt.setInt(1, id);
                    stmt.execute();
                }
                };

        Spark.get(
                "/selectRestaurant",
                ((request, response) -> {

                    String restaurantName = request.queryParams("restaurantName");
                    ArrayList<RestaurantList> (Connection conn) throws SQLException {
                        ArrayList<RestaurantList> restaurants = new ArrayList<>();
                        Statement stmt = conn.createStatement();
                        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
                        while (results.next()) {
                            int id = results.getInt("id");
                            String text = results.getString("text");
                            boolean isDone = results.getBoolean("is_Open");
                            double isOpen = results.getDouble("rating");
                            restaurants.add(new RestaurantList(id, text, isOpen, rating));
                        }
                        return restaurants;
                    }
                    )};

                    Spark.post(
                            "/UpdateRestaurant",
                            ((request, response) ->
                            {
                                public static void deleteRestaurant (Connection conn,int id) throws SQLException {
                                PreparedStatement stmt = conn.prepareStatement("DELETE restaurant SET is_Open = False is_Open WHERE id = ?");
                                stmt.setInt(1, id);
                                stmt.execute();
                            }
                            }));

                    Spark.get(
                            "/",
                            ((request, response) -> {
                                HashMap m = new HashMap<>();
                                m.put(1, "Olive Garden" );
                                return new ModelAndView(m, "home.html");
                            },
                            new MustacheTemplateEngine()
                            }));
}
}

