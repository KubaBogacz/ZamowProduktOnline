package app;

import database.DBConnection;
import users.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class Functions {

    // Funkcja do logowania. Zwraca Usera - mozna ustawic w petli Appki jako ActiveUser, czy cos - zeby sledzic, ktorym userem jest uzytkownik
    public User login(String email, String password, List<User> userList) {
        for (User checkedUser : userList) {
            if (Objects.equals(checkedUser.getEmail(), email) && Objects.equals(checkedUser.getPassword(), password)) {
                System.out.printf("Zalogowano pomyślnie jako: %s.", checkedUser.getEmail());
                return checkedUser;
            }
        }
        System.out.println("Podana kombinacja emailu oraz hasła jest nieprawidłowa!");
        return null;
    }

    // Funkcja do sprawdzania, czy email istnieje w DB - jesli true, odmawiamy zalozenia konta, jesli false - dopuszczamy
    public boolean checkIfEmailTaken(String email, List<User> userList) {
        for (User checkedUser : userList) {
            if (Objects.equals(checkedUser.getEmail(), email)) {
                return true;
            }
        }
        return false;
    }


    public static void showCategories() {
        String sql = "SELECT name FROM zpo.categories";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsShowCategories = preparedStatement.executeQuery();
                System.out.println("Kategorie: ");
                printResultSet(rsShowCategories);
            }
        } catch (SQLException e) {
            System.out.println("Error printing categories: " + e.getMessage());
        }
    }


    public static void showCategoryProducts(String categoryName) {
        String sql = "SELECT products.name FROM zpo.products\n" +
                "INNER JOIN categories\n" +
                "ON products.category_id = categories.id\n" +
                "WHERE categories.name = '" + categoryName + "';";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsShowProducts = preparedStatement.executeQuery();
                System.out.println("Przedmioty z kategorii " + categoryName + ": ");
                printResultSet(rsShowProducts);
            }
        } catch (SQLException e) {
            System.out.println("Error printing products: " + e.getMessage());
        }
    }

    public static void showProductInfo(String productName) {
        String sql = "SELECT products.name, products.description, products.price FROM zpo.products" +
                " WHERE products.name = '" + productName + "';";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsProductInfo = preparedStatement.executeQuery();
                System.out.println("Opis: ");
                printResultSet(rsProductInfo);
            }
        } catch (SQLException e) {
            System.out.println("Error printing product info: " + e.getMessage());
        }
    }



    public static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount(); // liczba kolumn
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(", ");
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
        System.out.println("");

    }

}
