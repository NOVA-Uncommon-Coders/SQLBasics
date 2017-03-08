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

    public static void insertRestaurant(Connection conn, Restaurant intoSQL) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES (NULL, ?, ?, ?, ?)");
        stmt.setString(1, intoSQL.getName());
        stmt.setString(2, intoSQL.getRating());
        stmt.setString(3, intoSQL.getSignatureDish());
        stmt.setString(4, intoSQL.getHealthGrade());
        stmt.execute();
    }

    public static void updateRestaurant(Connection conn, String editWhat, Restaurant updated) throws SQLException{
        PreparedStatement stmt;
        if(!updated.getName().isEmpty()){
            stmt=conn.prepareStatement("UPDATE restaurants SET RESTAURANTNAME = ? WHERE restaurantname = ?");
            stmt.setString(1,updated.getName());
            stmt.setString(2,editWhat);
            stmt.execute();
        }
        if(!updated.getRating().isEmpty()){
            stmt=conn.prepareStatement("UPDATE restaurants SET rating = ? WHERE restaurantname = ?");
            stmt.setString(1,updated.getRating());
            stmt.setString(2,editWhat);
            stmt.execute();
        }
        if(!updated.getSignatureDish().isEmpty()){
            stmt=conn.prepareStatement("UPDATE restaurants SET sigdish = ? WHERE restaurantname = ?");
            stmt.setString(1,updated.getSignatureDish());
            stmt.setString(2,editWhat);
            stmt.execute();
        }
        if(!updated.getHealthGrade().equals(null)){
            stmt=conn.prepareStatement("UPDATE restaurants SET healthrating = ? WHERE restaurantname = ?");
            stmt.setString(1,updated.getHealthGrade());
            stmt.setString(2,editWhat);
            stmt.execute();
        }
    }

    public static void deleteRestaurant(Connection conn, String restaurantName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE restaurantName = ?");
        stmt.setString(1, restaurantName);
        stmt.execute();
    }

    public static ArrayList<Restaurant> selectRestaurants(Connection conn) throws SQLException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("restaurantname").toUpperCase();
            String rating = results.getString("rating");
            String signatureDish = results.getString("sigDish");
            String healthRating = results.getString("healthRating").toUpperCase();
            restaurants.add(new Restaurant(name, rating, signatureDish,healthRating));
        }
        return restaurants;
    }
    public static boolean thereAreDuplicates(Connection conn, String name) throws SQLException{
        boolean same = false;
        ArrayList<Restaurant> restaurants= new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results=stmt.executeQuery("SELECT restaurantname FROM restaurants");
        while (results.next()) {
            if (results.getString("restaurantname").equalsIgnoreCase(name)){
                same=true;
            }
        }
        return same;
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, restaurantname VARCHAR, rating VARCHAR, sigDish VARCHAR, healthRating VARCHAR)");
        Spark.staticFileLocation("/templates");

        Spark.init();

        Spark.get("/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    ArrayList<Restaurant> restaurants;
                    restaurants=selectRestaurants(conn);
//                    for (Restaurant looping : restaurants) {
//                        if (looping.getName() == replyIdNum) {
//                            threads.put(message);
//                        }
//                    }
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    m.put("restaurant", restaurants);
                    m.put("userName", userName);
                    return new ModelAndView(m,"home.html");
                }),new MustacheTemplateEngine()
                );
        Spark.post("/create-restaurant",
                ((request, response) -> {
                    String name=request.queryParams("name").toUpperCase();
                    String rating=request.queryParams("rating");
                    String signatureDish=request.queryParams("sigDish").toLowerCase();
                    String healthGrade=request.queryParams("healthGrade").toUpperCase();
                    if(!rating.contains("*")){
                        response.redirect("/");
                        return "";
                    }
                    if(healthGrade.length()!=1){
                        response.redirect("/");
                        return "";
                    }
//                    ArrayList<Restaurant> restaurants;
//                    restaurants=selectRestaurants(conn);
//                    for (Restaurant looping : restaurants) {
//                        if (looping.getName().equalsIgnoreCase(name)){
//                            response.redirect("/create-restaurant");
//                            return"";
//                        }
//                    }
                    if(thereAreDuplicates(conn,name)){
                        response.redirect("/");
                        return "";
                    }
                    Restaurant input = new Restaurant(name,rating,signatureDish,healthGrade);
                    insertRestaurant(conn,input);

                    response.redirect("/");
                    return"";
                })
                );

        Spark.post("/delete-restaurant",
                ((request, response) -> {
                    String deleteThisRestaurant = request.queryParams("deleteRes").toUpperCase();

                    deleteRestaurant(conn,deleteThisRestaurant);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post("/update-restaurant",
                ((request, response) -> {
                    String changeThis = request.queryParams("whichEdit").toUpperCase();
                    String newName = request.queryParams("editName").toUpperCase();
                    String newRating = request.queryParams("editRating");
                    String newSigDish = request.queryParams("editSigDish").toUpperCase();
                    String newGrade = request.queryParams("editHealthGrade").toUpperCase();
                    if(!thereAreDuplicates(conn,changeThis)){
                        response.redirect("/");
                        return"";
                    }
//                    if(newName.isEmpty()||newRating.isEmpty()||newSigDish.isEmpty()||newGrade.isEmpty()){
//
//                        Restaurant updated = new Restaurant(newName,newRating,newSigDish,newGrade);
//                        updateRestaurant(conn,changeThis,updated);
//                        response.redirect("/");
//                        return "";
//                    }
                    Restaurant updated = new Restaurant(newName,newRating,newSigDish,newGrade);
                    updateRestaurant(conn,changeThis,updated);

                    response.redirect("/");
                    return "";
                })
        );
    }
}
