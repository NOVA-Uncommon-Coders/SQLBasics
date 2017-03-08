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
    public static boolean edit = false;
    public static boolean search = false;
    public static ArrayList<Restaurant> globalRestaurants;
    public static HashMap<String, User> user = new HashMap<>();

    public static void main(String[] args) throws SQLException{


        Server.createWebServer().start();
        Connection connection = DriverManager.getConnection("jdbc:h2:./main");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS restaurants(id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER)");

        globalRestaurants = selectRestaurants(connection);

        Spark.staticFileLocation("/styles");
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap<>();

                    Session session = request.session();
                    String loginName = session.attribute("userName");
                    if (loginName != null){
                        user.get(loginName).setHomePage(true);
                        user.get(loginName).setViewAllPage(false);
                        m.put("user", user.get(loginName));
                    }
                    if (session.attribute("badPass") != null){
                        m.put("badPass", "badPass");
                    }
                    return new ModelAndView(m,"home.html" );
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/logout.html",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    if (session.attribute("userName") != null){
                        String userName = session.attribute("userName");
                        user.get(userName).setViewAllPage(false);
                        user.get(userName).setHomePage(false);
                    }
                    session.invalidate();
                    return new ModelAndView(m, "home.html");

                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/viewall.html",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (user.get(userName) != null){
                        user.get(userName).setViewAllPage(true);
                        user.get(userName).setHomePage(false);
                        m.put("user", user.get(userName));
                    }
                    if (!edit && !search) {
                        //Refresh if you are not in edit mode AND not in search
                        globalRestaurants = selectRestaurants(connection);
                    }
                    m.put("restaurants", globalRestaurants);

                    return new ModelAndView(m,"home.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/home.html",
                ((request, response) -> {
                    globalRestaurants = selectRestaurants(connection);
                    HashMap m = new HashMap();
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    if (user.get(userName) != null){
                        user.get(userName).setViewAllPage(false);
                        user.get(userName).setHomePage(true);
                        m.put("user", user.get(userName));
                    }
                    return new ModelAndView(m, "home.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    Session session = request.session();
                    String name = request.queryParams("userName");
                    String password = request.queryParams("password");


                    session.removeAttribute("badPass");
                    if(user.containsKey(name)){
                        if (!(user.get(name).getPassword().equals(password))){
                            session.attribute("badPass", "badPass");
                            response.redirect("/");
                            return "";
                        }
                    }
                    user.putIfAbsent(name, new User(name, password));
                    session.attribute("userName", name);
                    response.redirect("/");
                    return"";
                })
        );
        Spark.post(
                "/add-location",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (user.get(userName) == null){
                        response.redirect("/");
                        return "";
                    } else {
                        String[] information = new String[5];
                        int zip;
                        information[0] = request.queryParams("name");
                        information[1] = request.queryParams("type");
                        information[2] = request.queryParams("street");
                        information[3] = request.queryParams("city");
                        information[4] = request.queryParams("state");

                        zip = Integer.valueOf(request.queryParams("zip"));
                        insertRestaurant(connection, information, zip);
                        response.redirect("/home.html");
                        return "";
                    }

                })
        );
        Spark.post(
                "/edit",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        int id = Integer.valueOf(request.queryParams("id"));
                        for (Restaurant restaurant: globalRestaurants){
                            restaurant.setEdit(false);
                            restaurant.setDisplay(true);
                            if (restaurant.getId() == id){
                                restaurant.setEdit(true);
                                restaurant.setDisplay(false);
                                edit = true;
                            }
                        }
                        response.redirect("/viewall.html");
                        return "";

                    }
                })
        );
        Spark.post(
                "/edit-restaurant",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        int id = Integer.valueOf(request.queryParams("id"));

                        //0:street 1:city 2:state 3:name 4:type
                        String[] information = new String[5];
                        int zip;
                        information[0] = request.queryParams("name");
                        information[1] = request.queryParams("type");
                        information[2] = request.queryParams("street");
                        information[3] = request.queryParams("city");
                        information[4] = request.queryParams("state");

                        zip = Integer.valueOf(request.queryParams("zip"));
                        updateRestaurant(connection, id, information, zip);
                        edit = false;
                        search = false;
                        for (Restaurant restaurant: globalRestaurants){
                            restaurant.setEdit(false);
                            restaurant.setDisplay(true);
                        }
                        response.redirect("/viewall.html");
                        return "";
                    }
                })
        );
        Spark.post(
                "/cancel-edit",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        for (Restaurant restaurant : globalRestaurants) {
                            restaurant.setEdit(false);
                            restaurant.setDisplay(true);
                        }
                        edit = false;
                        response.redirect("/viewall.html");
                        return "";
                    }
                })
        );
        Spark.post(
                "/delete",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        int id = Integer.valueOf(request.queryParams("id"));
                        deleteRestaurant(connection, id);
                        response.redirect("/viewall.html");
                        return "";
                    }
                })
        );
        Spark.post(
                "/search",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        String search = request.queryParams("search").toLowerCase();
                        user.get(userName).setSearch(true);
                        for(Restaurant restaurant: globalRestaurants){
                            restaurant.setDisplay(false);
                            String name = restaurant.getName().toLowerCase();
                            if (name.contains(search)){
                                restaurant.setDisplay(true);
                            }
                        }
                        Main.search = true;
                        response.redirect("/viewall.html");
                        return "";
                    }
                })
        );
        Spark.post(
                "/cancel-search",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null){
                        response.redirect("/");
                        return "";
                    } else {
                        user.get(userName).setSearch(false);
                        Main.search = false;
                        response.redirect("/viewall.html");
                        return "";
                    }
                })
        );


    }
    public static int insertRestaurant(Connection connection, String[] information, int zip) throws SQLException{
        //id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER
        PreparedStatement statement = connection.prepareStatement("INSERT INTO restaurants VALUES(NULL, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, information[0]);
        statement.setString(2, information[1]);
        statement.setString(3, information[2]);
        statement.setString(4, information[3]);
        statement.setString(5, information[4]);
        statement.setInt(6, zip);
        statement.execute();
        PreparedStatement getId = connection.prepareStatement("SELECT id FROM restaurants");
        ResultSet results = getId.executeQuery();

        int id = 0, currentId = 0;
        while (results.next()){
            currentId = results.getInt("id");
            if (currentId > id){
                id = currentId;
            }
        }
        return id;

    }
    public static void deleteRestaurant(Connection connection, int id) throws SQLException{
        PreparedStatement delete = connection.prepareStatement("DELETE FROM restaurants WHERE id = ?");
        delete.setInt(1, id);
        delete.execute();
    }
    public static ArrayList<Restaurant> selectRestaurants(Connection connection) throws SQLException{
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM restaurants");
        while (results.next()) {
            //id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER
            int id = results.getInt("id");
            String name = results.getString("name");
            String type = results.getString("type");
            String street  = results.getString("street");
            String city = results.getString("city");
            String state = results.getString("state");
            int zip = results.getInt("zip");

            restaurants.add(new Restaurant(id, name, type, street, city, state, zip));
        }
        return restaurants;
    }
    public static void updateRestaurant(Connection connection, int id, String[] information, int zip) throws SQLException{
        //id IDENTITY, name VARCHAR, type VARCHAR, street VARCHAR, city VARCHAR, state VARCHAR, zip INTEGER
        PreparedStatement update = connection.prepareStatement("UPDATE restaurants SET name = ?, type = ?, street = ?, city = ?, state = ?, zip = ? WHERE id = ?");
        update.setString(1, information[0]);
        update.setString(2, information[1]);
        update.setString(3, information[2]);
        update.setString(4, information[3]);
        update.setString(5, information[4]);
        update.setInt(6, zip);
        update.setInt(7, id);
        update.execute();
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
