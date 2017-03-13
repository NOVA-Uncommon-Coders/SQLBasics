package com.theironyard.novauc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jerieshasmith on 3/12/17.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;

    }
    @Test
    public void testUser() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn,"","");
        User user = Main.selectUser(conn,"");
        conn.close();
        assertTrue( user == null);
    }
@Test
public void testResults() throws SQLException {
    Connection conn = startConnection();
    Main.insertUser(conn, "", "");
    Main.insertUser(conn, "", "");
    Main.insertRestaurant(conn,1,"","","");
    ArrayList<Restaurant> results = Main.selectResults(conn);
    conn.close();
    assertTrue(results.size() == 1);
}



}

