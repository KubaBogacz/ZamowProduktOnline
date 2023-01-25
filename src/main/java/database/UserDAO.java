package database;

import users.Buyer;
import users.Seller;
import users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Funkcja zwracajaca ArrayList Users nie przyjmujac zadnych argumentow - do wywolania na starcie programu
public class UserDAO {
    public static List<User> importUsers() {
        String sql = "SELECT * FROM zpo.users";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsUsers = preparedStatement.executeQuery();
                return createUserList(rsUsers);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<User> createUserList(ResultSet rsUsers) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (rsUsers.next()) {
            if (Objects.equals(rsUsers.getString(7), "buyer")) {
                userList.add(new Buyer(rsUsers.getInt(1), rsUsers.getString(2), rsUsers.getString(3), rsUsers.getString(4), rsUsers.getString(5), rsUsers.getInt(6)));
            } else if (Objects.equals(rsUsers.getString(7), "seller")) {
                userList.add(new Seller(rsUsers.getInt(1), rsUsers.getString(2), rsUsers.getString(3), rsUsers.getString(4), rsUsers.getString(5), rsUsers.getInt(6)));
            }
        }
        return userList;
    }
}
