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
static HashMap<String, User> users = new HashMap<>();
static ArrayList<Restaurant> restaurants = new ArrayList<>();




    public static ArrayList<Restaurant> selectResults(Connection conn) throws SQLException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT FROM restaurants INNER  JOIN users ON restaurants.user_id = users.id WHERE restaurants = ?");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("restaurants.id");
            String name = results.getString("users.userName");
            String type = results.getString("restaurants.type");
            String location = results.getString("restaurants.location");
            String cuisine = results.getString("restaurants.cuisine");
            Restaurant restaurant = new Restaurant(type, location, cuisine);
            restaurants.add(restaurant);

        }
        return restaurants;
    }


    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants(id IDENTITY, user_id INT,type VARCHAR, location VARCHAR,cuisine VARCHAR)");

    }

    public static void  insertUser(Connection conn, String userName, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL,?,?");
        stmt.setString(1, userName);
        stmt.setString(2, password);
        stmt.execute();
    }



    public static User selectUser(Connection conn, String username) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1,username);
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id,username,password);
        }
        return null;
    }

    public static void insertRestaurant(Connection conn, int userId, String type, String location,String cuisine) throws SQLException{

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants VALUES(NULL,?,?,?,?");
        stmt.setInt(1,userId);
        stmt.setString(2,type);
        stmt.setString(3,location);
        stmt.setString(4, cuisine);
        stmt.execute();
    }



    public static void selectRestaurants (Connection conn) throws SQLException{

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restaurants INNER JOIN users ON restaurants user_id = users.id WHERE restaurants.id = ?");

        ResultSet results = stmt.executeQuery();
        if(results.next()){
            String type = results.getString("type");
            String location = results.getString("location");
            String cuisine = results.getString("cuisine");

            restaurants.add(new Restaurant(type,location,cuisine));
        }


    }


    public static void deleteRestaurants(Connection conn,int id ,String type, String location, String cuisine) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restaurants WHERE id = ?");
        stmt.setInt(1, id);
        stmt.setString(2,type);
        stmt.setString(3,location);
        stmt.setString(4,cuisine);
        stmt.execute();

    }



    public static Restaurant updateResturant(Connection conn,int id,String type,String location,String cuisine) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE FROM restaurants WHERE id = ?");
        stmt.setInt(1,id);
        stmt.setString(2,type);
        stmt.setString(3,location);
        stmt.setString(4,cuisine);
        stmt.execute();
        return null;
    }
    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);




    Spark.init();


    Spark.get("/",((request, response) -> {
                Session session = request.session();
                String userName = session.attribute("userName");
                User user = users.get(userName);


        HashMap r = new HashMap();
        ArrayList<Restaurant> reply = selectResults(conn);
        r.put("restaurants",reply) ;
        r.put("userName", userName);




        return new ModelAndView(r,"home.html");


            }),
            new MustacheTemplateEngine()


    );

    Spark.post("/create-restaurant",((request, response) -> {

                Session session = request.session();
                String userName = session.attribute("userName");
                String password = session.attribute("password");
                User user = users.get(userName);


        String type = request.queryParams("restaurantType");
        String location = request.queryParams("restaurantLocation");
        String cuisine = request.queryParams("restaurantCuisine");
        User u = selectUser(conn,userName);
        insertRestaurant(conn, user.getId(),type,location,cuisine);
        Restaurant r = new Restaurant(type,location,cuisine);
        restaurants.add(r);


            response.redirect("/");
            return"";





            })
    );

        Spark.post("/delete", ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    User user = selectUser(conn,userName);
                    deleteRestaurants(conn,user.getId(),"","","");
                        restaurants.remove(1);
                    response.redirect("/");
                    return "";

                })
        );












        Spark.post(
                "/login",
                ((request, response) -> {
                    String userName = request.queryParams("loginName");
                    if (userName == null) {
                        throw new Exception("Login name not found.");
                    }

                    User user = selectUser(conn, userName);
                    if (user == null) {
                        insertUser(conn, userName, "");
                    }


                    Session session = request.session();
                    session.attribute("userName", userName);

                    response.redirect("/");
                    return "";
                })
        );


        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );



}
}