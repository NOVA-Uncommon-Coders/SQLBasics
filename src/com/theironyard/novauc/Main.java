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

//    public static void insertRestaurant(Connection conn, String restaurantName, Boolean is_tasty, int numWaiters) throws SQLException {
//        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO restaurants VALUES " +
//                "(NULL, ?, ?, ?)");
//        pstmt.execute();
//    }
    public static HashMap<String, User> accountInfo = new HashMap<>();
    public static ArrayList<Restaurant> entries = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
//        Server.createWebServer().start();
//
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//        Statement stmt = conn.createStatement();
//        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, restaurantName VARCHAR(100), " +
//                " is_tasty BOOLEAN, numWaiters INT)");

        Spark.init();

        //TODO set entries to accept all three html fields, not just the first

        Spark.get("/", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute("userName");
            HashMap userActivity = new HashMap();
            if(!accountInfo.containsKey(name)) {
                return new ModelAndView(userActivity,"index.html");
                }
            else {
                  userActivity.put("entries", entries);
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
            return "failure at the end of /login";
        });

        Spark.post("/create-restaurant", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute("userName");
            String message = request.queryParams("newEntry");

            String restaurantName = request.queryParams("restaurantName");
            Boolean tasty = Boolean.getBoolean(request.queryParams("restaurantTasty"));
            int numWaiters = Integer.valueOf(request.queryParams("restaurantNumWaiters"));

            Restaurant entryObj = new Restaurant(restaurantName, tasty, numWaiters);
            entries.add(entryObj);


            /*insertRestaurant(conn, name, tasty ,numWaiters);*/


            response.redirect("/");
            return "";
        }));

        Spark.post("/edit-restaurant", (request, response) -> {
            String editor = request.queryParams("editMessageT");

            int edit = Integer.valueOf(request.queryParams("messID"));

            Restaurant entrance = null;
            for (Restaurant picker : entries) {
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

        /*
        Spark.get("/anotherplace/:id", ((request, response) -> {
                    String idJunk = request.params("id");
                    HashMap whatever = new HashMap();
                    whatever.put("id",idJunk);
                    return new ModelAndView(whatever, "anotherplace.html");
                }), new MustacheTemplateEngine()
        );

       */

        Spark.post("/delete-restaurant", (request, response) -> {
            int delete = Integer.valueOf(request.queryParams("messDel"));
            Restaurant entrance = new Restaurant();
            for (Restaurant picker : entries) {
                if (/*picker.getId()*/ 1 ==  delete) {
                    entrance = picker;
                }
            }
            entries.remove(entrance);
            response.redirect("/");
            return "";
        });

        Spark.post("/logout", ((request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
        }));
    }
}

