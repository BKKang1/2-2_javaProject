package persistence;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil { // Connection DB

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/project?characterEncoding=utf8", "root" , "Dh1029Hj!@");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}