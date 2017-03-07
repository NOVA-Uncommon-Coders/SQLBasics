package com.theironyard.novauc;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException{
        HashMap<String, User> user = new HashMap<>();
        Server.createWebServer().start();
        Connection connection = DriverManager.getConnection("jdbc:h2:./main");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS resturants(id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER)");

        Spark.staticFileLocation("/styles");
        Spark.init();
        HashMap m = new HashMap();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String loginName = session.attribute("userName");
                    return new ModelAndView(m,"home.html" );
                }),
                new MustacheTemplateEngine()
        );


    }
    public static void insertRestaurant() throws SQLException{

    }
    public static void deleteRestaurant() throws SQLException{

    }
//    public static ArrayList<Restaurant> selectRestaurants() throws SQLException{
//
//    }
    public static void updateRestaurant() throws SQLException{

    }
}
/**
 TODO: Create the Connection and execute a query to create a restaurants table that stores the restaurant name and other attributes.
 TODO: Write a static method insertRestaurant and run it in the /create-restaurant route. It should insert a new row with the user-supplied information.
 TODO: Write a static method deleteRestaurant and run it in the /delete-restaurant route. It should remove the correct row using id.
 TODO: Write a static method selectRestaurants that returns an ArrayList<Restaurant> containing all the restaurants in the database.
 TODO: Remove the global ArrayList<Restaurant> and instead just call selectRestaurants inside the "/" route.
 TODO: Add a form to edit the restaurant name and other attributes, and create an /edit-restaurant route.
 TODO: Write a static method updateRestaurant and use it in that route. Then redirect to "/".
 TODO: Optional: Add a search form which filters the restaurant list to only those restaurants whose name contains the (case-insensitive) search string.
 */
