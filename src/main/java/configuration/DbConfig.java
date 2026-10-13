package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    private static final String url = "jdbc:postgresql://localhost:5432/YucaDb";
    private static final String user = "postgres";
    private static final String password = "enterYourPassword";

    public static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
          if (con == null){
              throw new IllegalArgumentException("Database connection returned null.");
          }
          return con;
        }catch (SQLException | ClassNotFoundException e){
            throw new IllegalArgumentException("Failed to connect to database: " + e.getMessage(), e);
        }
    }
}

//  return DriverManager.getConnection(url, user, password);
//        } catch (SQLException e) {
//        throw new RuntimeException(e);