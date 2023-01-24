package database;

import products.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean checkLoginInfo(String email, String password) {
        String sql = "SELECT COUNT(id) FROM users WHERE email = " + email + "AND password = " + password;
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsLoginSucceedInfo = preparedStatement.executeQuery();
                return rsLoginSucceedInfo.getInt(0) != 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking login info: " + e.getMessage());
            return false;
        }
    }
}
