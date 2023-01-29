package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String connectionUrl =
                    "jdbc:mysql://" +
                    "localhost:3306/" +                         // numer portu
                    "zpo?" +                                    // nazwa bazy
                    "useUnicode=true&characterEncoding=utf-8" + // kodowanie
                    "&user=root" +                              // nazwa uzytkownika
                    "&password=P4ssworD" +                      // haslo uzytkownika
                    "&serverTimezone=CET";                      // strefa czasowa (CET)
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection acquired ");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}