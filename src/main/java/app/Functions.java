package app;

import database.CategoriesDAO;
import database.ProductDAO;
import database.ReviewsDAO;
import products.Product;
import users.Buyer;
import users.Seller;
import users.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;

import static database.UserDAO.addUser;

public class Functions {

    /***
     * Metoda obsługująca logowanie na konto
     * @param email - email użytkownika
     * @param password - hasło użytkownika
     * @param userList - lista użytkowników
     * @return User - w przypadku powodzenia, użytkownik o zgodnym emailu i haśle z podanymi
     */
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

    /***
     * Metoda sprawdzająca, czy dany adres email jest zajęty przez inne konto
     * @param email - sprawdzany email
     * @param userList - lista użytkowników
     * @return boolean - informacja o tym, czy email jest zajęty (true) czy nie (false)
     */
    public boolean checkIfEmailTaken(String email, List<User> userList) {
        for (User checkedUser : userList) {
            if (Objects.equals(checkedUser.getEmail(), email)) {
                return true;
            }
        }
        return false;
    }

    /***
     * Metoda tworząca nowe konto użytkownika
     * @param id - id nowego użytkownika
     * @param email - email nowego użytkownika
     * @param password - hasło nowego użytkownika
     * @param name - imię nowego użytkownika
     * @param surname - nazwisko nowego użytkownika
     * @param telNumber - nr telefonu nowego użytkownika
     * @param userType - typ nowego użytkownika ("buyer"/"seller")
     * @param userList - lista wszystkich użytkowników
     * @param connection - połączenie z BD
     * @return boolean - informacja o powodzeniu działania
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public boolean createAccount(int id, String email, String password, String name,
                                 String surname, int telNumber, String userType, List<User> userList, Connection connection) throws SQLException {
        if (checkIfEmailTaken(email, userList)) { // Sprawdzanie, czy podany email jest zajęty
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
                addUser(newUser, connection);
                // Od razu dodajemy do listy użytkowników
                userList.add(newUser);
            } else if (Objects.equals(userType, "seller")) {
                Seller newUser = new Seller();
                newUser.setId(id);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setName(name);
                newUser.setSurname(surname);
                newUser.setTelNumber(telNumber);
                addUser(newUser, connection);
                // Od razu dodajemy do listy użytkowników
                userList.add(newUser);
            }
            return true;
        }
    }

    /***
     * Metoda drukująca kategorie dostępne w sklepie
     * @param categoriesList - zaimportowana z BD lista kategorii
     */
    public static void showCategories(List<String> categoriesList) {
        int increment = 1;
        System.out.println("\nLista kategorii:");
        if (!categoriesList.isEmpty()) {
            for (String categoryName : categoriesList) {
                System.out.println((increment++) + ". " + categoryName);
            }
            System.out.print("\n");
        } else {
            System.out.println("Lista kategorii jest pusta.");
        }
    }
    /***
     * Metoda drukująca listę produktów z danej kategorii
     * @param categoryName - nazwa kategorii
     * @param productList - lista produktów zaimportowana z BD
     * @return boolean - informacja o powodzeniu działania
     */
    public static boolean showCategoryProducts(String categoryName, List<Product> productList) {
        boolean productFound = false;
        for (Product product : productList) {
            if (product.getCategory().toLowerCase().equals(categoryName)) {
                System.out.println("ID produktu: " + product.getId() + ", Nazwa produktu: " + product.getName());
                productFound = true;
            }
        }
        if (!productFound) {
            System.out.println("Brak produktów w podanej kategorii");
        }
        return productFound;
    }

    /***
     * Metoda wyświetlająca informacje o danym produkcie.
     * @param productID - ID produktu
     * @param productList - lista produktów
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void showProductInfo(int productID, List<Product> productList, Connection connection) throws SQLException {
        for (Product product : productList) {
            if (product.getId() == productID) {
                System.out.println(product.toString());
                System.out.println("Liczba sprzedanych sztuk: " + ProductDAO.getSoldAmount(product, connection));
                System.out.println("Średnia ocena: " + ReviewsDAO.avgReviewRating(product, connection));
            }
        }
    }

    /***
     * Metoda drukująca wyniki SELECT z BD MySQL
     * @param resultSet - tabela do wydrukowania
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
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
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}
