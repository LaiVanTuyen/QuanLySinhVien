
package model;
import org.jetbrains.annotations.Nullable;

import java.sql.*;


public class Service {
    public static @Nullable Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url ="jdbc:mysql://localhost:3306/quanlysinhvien";
            String user = "root";
            String password = "240301";
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException  | SQLException ex) {
            return null;
        }
    }
}
