package app;

import database.DBConnection;
import products.Product;
import users.Buyer;
import users.Seller;
import users.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;

import static database.UserDAO.addUser;

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

    // Funcja do tworzenia nowego konta, wykorzystuje checkIfEmailTaken
    public boolean createAccount(int id, String email, String password, String name,
                                 String surname, int telNumber, String userType, List<User> userList) {
        if (checkIfEmailTaken(email, userList)) {
            System.out.println("Podany email jest już zajęty!");
            return false;
        } else {
            if (Objects.equals(userType, "buyer")) {
                Buyer newUser = new Buyer();
                newUser.setId(id);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setName(name);
                newUser.setSurname(surname);
                newUser.setTelNumber(telNumber);
                addUser(newUser);
                // Od razu dodajemy do listy, nie trzeba odświeżać po dodaniu użytkownika
                userList.add(newUser);
            } else if (Objects.equals(userType, "seller")) {
                Seller newUser = new Seller();
                newUser.setId(id);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setName(name);
                newUser.setSurname(surname);
                newUser.setTelNumber(telNumber);
                addUser(newUser);
                // Od razu dodajemy do listy, nie trzeba odświeżać po dodaniu użytkownika
                userList.add(newUser);
            }
            return true;
        }
    }

    // Funkcja do wyświetlania kategorii produktów
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


    // Funkcja do wyświetlania produktów z danej kategorii na podstawie otrzymanej listy produktów
    public static void showCategoryProducts(String categoryName, List<Product> productList) {
        for (Product product : productList) {
            if (product.getCategory().equals(categoryName)) {
                System.out.println(product.getName());
            }
        }
    }

    // Funkcja do wyświetlania informacji o danym produkcie
    public static void showProductInfo(String productName, List<Product> productList) {
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
                System.out.println(product.toString());
            }
        }
    }

    // Funkcja do drukowania wyników komend z MySQL
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
