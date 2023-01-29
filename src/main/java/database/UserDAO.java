package database;

import products.Cart;
import users.Buyer;
import users.Seller;
import users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.Functions.printResultSet;

public class UserDAO {

    /***
     * Metoda dodająca użytkownika do BD
     * @param user - nowy użytkownik
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void addUser(User user, Connection connection) throws SQLException {
        Cart cart = new Cart();
        cart.setUserId(user.getId());
        CartDAO.addCart(cart, connection);
        String sql = "INSERT INTO zpo.users (id, email, password, name, surname, phone_number, user_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getName());
        preparedStatement.setString(5, user.getSurname());
        preparedStatement.setInt(6, user.getTelNumber());
        String userType = null;
        if (user instanceof Buyer) {
            userType = "buyer";
        } else if (user instanceof Seller) {
            userType = "seller";
        }
        preparedStatement.setString(7, userType);
        preparedStatement.executeUpdate();
    }

    /***
     * Metoda importująca importująca listę użytkowników i zwracająca ich listę
     * @param connection - połączenie z BD
     * @return List<User> - Lista użytkowników
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static List<User> importUsers(Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.users";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsUsers = preparedStatement.executeQuery();
        return createUserList(rsUsers);
    }

    /***
     * Metoda tworząca listę użytkowników na podstawie ResultSetu użytkowników z BD
     * @param rsUsers - ResultSet użytkowników z BD
     * @return List<User> - Lista użytkowników
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    private static List<User> createUserList(ResultSet rsUsers) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (rsUsers.next()) {
            if (Objects.equals(rsUsers.getString(7), "buyer")) {
                Buyer user = new Buyer();
                user.setId(rsUsers.getInt(1));
                user.setEmail(rsUsers.getString(2));
                user.setPassword(rsUsers.getString(3));
                user.setName(rsUsers.getString(4));
                user.setSurname(rsUsers.getString(5));
                user.setTelNumber(rsUsers.getInt(6));
                userList.add(user);
            } else if (Objects.equals(rsUsers.getString(7), "seller")) {
                Seller user = new Seller();
                user.setId(rsUsers.getInt(1));
                user.setEmail(rsUsers.getString(2));
                user.setPassword(rsUsers.getString(3));
                user.setName(rsUsers.getString(4));
                user.setSurname(rsUsers.getString(5));
                user.setTelNumber(rsUsers.getInt(6));
                userList.add(user);
            }
        }
        return userList;
    }

    /***
     * Metoda zwracająca historię zakupów użytkownika
     * @param userId - ID sprawdzanego użytkownika
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void showUserOrderHistory(int userId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.orders WHERE user_id = " + userId + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsOrders = preparedStatement.executeQuery();
        printResultSet(rsOrders);
    }
}
