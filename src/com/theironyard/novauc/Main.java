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

    public static void insertRestaurant(String restName, String bestDish, int restNumWaiters) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement
                ("INSERT INTO restaurants (restName, bestDish, restNumWaiters) VALUES (?, ?, ?)");
        //User user = currentUser();
        ps.setString(1,restName);
        ps.setString(2,bestDish);
        ps.setInt(3,restNumWaiters);
        ps.execute();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:./main");
    }

    public static void createTables() throws SQLException {
        Statement stated = getConnection().createStatement();
        stated.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, restName VARCHAR, bestDish VARCHAR, restNumWaiters INT )");
        stated.execute("CREATE TABLE IF NOT EXISTS user (id IDENTITY , userName VARCHAR, password VARCHAR)");
    }

    public static void deleteRestaurant(int id) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("DELETE FROM restaurants WHERE id=?");
        ps.setInt(1,id);
        ps.execute();
    }

    public static ArrayList<Restaurant> selectRestaurants() throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM restaurants ");
        ArrayList<Restaurant> restaurantsAL = new ArrayList<>();
        ResultSet results = ps.executeQuery();
        while (results.next()){
            int id = results.getInt("id");
            String restName = results.getString("restName");
            String bestDish = results.getString("bestDish");
            int restNumWaiters = results.getInt("restNumWaiters");

            restaurantsAL.add(new Restaurant(id, restName, bestDish, restNumWaiters));
        }
        return restaurantsAL;
    }

    public static void updateRestaurant(String restName, String bestDish, int restNumWaiters) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement
                ("UPDATE restaurants SET (restName, bestDish, restNumWaiters) VALUES (?, ?, ?)");
        //User user = currentUser();
        ps.setString(1,"restEditName");
        ps.setString(2,"restEditDish");
        ps.setInt(3,"restEditDish");
        ps.execute();
    }

    public static User currentUser() {
        return new User(1, "billyray");
    }

    public static HashMap<String, User> accountInfo = new HashMap<>();
    public static ArrayList<Restaurant> restAL = new ArrayList<>();

    public static void main(String[] args) throws SQLException {

        Spark.init();
        Server.createWebServer().start();
        createTables();

        Spark.get("/", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute("userName");
            HashMap userActivity = new HashMap();
            if(!accountInfo.containsKey(name)) {
                return new ModelAndView(userActivity,"index.html");
                }
            else {
                  userActivity.put("entries", selectRestaurants());
                  userActivity.put("userName", name);
                  return new ModelAndView(userActivity, "index.html");
                  }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post("/login", (request, response) -> {
            String name = request.queryParams("userName");
            String password = request.queryParams("passwordLogin");
            Session session = request.session();

            if(accountInfo.containsKey(name)) {
                if(password.equals(accountInfo.get(name).getPassword())) {
                    session.attribute("userName", name);
                }
            }
            else {
                session.attribute("userName", name);
                accountInfo.put(name, new User(name, password));
            }
            response.redirect("/");
            return "";
        });

        Spark.post("/create-restaurant", (request, response) -> {
//            Session session = request.session();
//            String name = session.attribute("userName");

            String restName = request.queryParams("restName");
            String bestDish = request.queryParams("bestDish");
            int restNumWaiters = Integer.valueOf(request.queryParams("restNumWaiters"));

//            Restaurant entryObj = new Restaurant(restName, bestDish, restNumWaiters);
//            restAL.add(entryObj);

            insertRestaurant(restName, bestDish,restNumWaiters);

            response.redirect("/");
            return "";
        });

        Spark.post("/edit-restaurant", (request, response) -> {
            //String editor = request.queryParams("editMessageT");

            //int edit = Integer.valueOf(request.queryParams("messID"));

            Restaurant entrance = null;
            for (Restaurant picker : restAL) {
                if (/*picker.getId()*/ 1 ==  edit) {
                    entrance = picker;
                    break;
                }
            }
            if (entrance != null && editor != null){
                //entrance.setText(editor);
            }


            response.redirect("/");
            return "";
        });

//        Spark.get("/anotherplace/:id", ((request, response) -> {
//                    String idJunk = request.params("id");
//                    HashMap whatever = new HashMap();
//                    whatever.put("id",idJunk);
//                    return new ModelAndView(whatever, "anotherplace.html");
//                }), new MustacheTemplateEngine()
//        );

        Spark.post("/delete-restaurant", (request, response) -> {
//            int delete = Integer.valueOf(request.queryParams("restDelete"));
//            Restaurant entrance = new Restaurant();
//            for (Restaurant picker : restAL) {
//                if (/*picker.getId()*/ 1 ==  delete) {
//                    entrance = picker;
//                }
//            }
//            restAL.remove(entrance);
            
            deleteRestaurant(Integer.valueOf(request.queryParams("restDelete")));

            response.redirect("/");
            return "";
        });

        Spark.post("/logout", (request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
        });
    }
}

