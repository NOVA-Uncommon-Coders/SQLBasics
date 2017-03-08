package com.theironyard.novauc;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;

import java.sql.*;

public class Main {

    public static void insertRestaurant(Connection conn, String text, Boolean is_tasty, int numWaiters) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO restaurants VALUES " +
                "(NULL, ?, ?, ?)");
        //pstmt.setString(2, text);
        //pstmt.setBoolean(3, false);
        //pstmt.setInt(4,0)
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, name VARCHAR(100), " +
                " is_tasty BOOLEAN, has_waiters BOOLEAN)");

        Spark.init();

        Spark.get("/", (request, response) -> {
            Session session = request.session();
            return new ModelAndView("info from the database", "index.html");
        });

        Spark.post("/create-restaurant", (request, response) -> {
            Session session = request.session();
            String name = request.queryParams("restaurantName");
            Boolean tasty = Boolean.getBoolean(request.queryParams("restaurantTasty"));
            int numWaiters = Integer.valueOf(request.queryParams("restaurantNumWaiters"));

            //PreparedStatement pstmt = conn.prepareStatement("INSERT INTO")
            insertRestaurant(conn, name, tasty ,numWaiters);
            return "";
        });

    }
}
