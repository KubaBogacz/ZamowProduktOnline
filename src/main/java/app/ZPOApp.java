package app;

import database.*;
import products.Cart;
import products.Product;
import users.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ZPOApp {

    private static Product getProductFromList(int productId, List<Product> productList) {
        for (Product listProduct : productList) {
            if (listProduct.getId() == productId) {
                return listProduct;
            }
        }
        return null;
    }
    public static void run() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;

            List<User> userList = UserDAO.importUsers(connection);
            for (User user : userList) {
                System.out.println(user.getEmail());
                System.out.println(user.getPassword());
                System.out.println(user.getId());
            }

            List<Product> productList = ProductDAO.importProducts(connection);

            Functions functions = new Functions();
            Scanner scanner = new Scanner(System.in);

            String userInputString;
            int userInputInt;
            double userInputDouble;

            User activeUser = null;
            Cart userCart = null;

            boolean running = true;
            boolean loggedIn = false;

            // Pętla
            while (running) {
                if (!loggedIn) {
                    System.out.println("Wciśnij 1, aby się zalogować, 2 aby utworzyć nowe konto, 0 aby zakończyć działanie programu.");
                    userInputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (userInputInt == 1) { // Logowanie użytkownika
                        System.out.println("Podaj E-Mail:");
                        String userEmail = scanner.nextLine();
                        System.out.println("Podaj hasło:");
                        String userPassword = scanner.nextLine();
                        activeUser = functions.login(userEmail, userPassword, userList);
                        if (activeUser != null) {
                            loggedIn = true;
                            userCart = CartDAO.importCart(activeUser.getId(), productList, connection);
                        }
                    } else if (userInputInt == 2) {  // Rejestracja użytkownika
                        System.out.println("Podaj E-Mail:");
                        String userEmail = scanner.nextLine();
                        System.out.println("Podaj hasło:");
                        String userPassword = scanner.nextLine();
                        System.out.println("Podaj imię:");
                        String userFirstName = scanner.nextLine();
                        System.out.println("Podaj nazwisko:");
                        String userLastName = scanner.nextLine();
                        System.out.println("Podaj numer telefonu:");
                        int userPhoneNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Określ, czy zamierzasz cokolwiek sprzedawać na ZPO.pl ('Tak'/'Nie'):");
                        String ans = scanner.nextLine();
                        String userType = "buyer";
                        if (Objects.equals(ans.toLowerCase(), "tak")) {
                            userType = "seller";
                        } else if (!Objects.equals(ans.toLowerCase(), "nie")) {
                            System.out.println("Wprowadzono nieprawidłową odpowiedź. Ustawiono wartość domyślną na: Kupujący.");
                        }
                        int lastID;
                        if (userList.size() == 0) {
                            lastID = 1;
                        } else {
                            lastID = userList.get(userList.size() - 1).getId() + 1;
                        }
                        boolean registerCheck = functions.createAccount(lastID, userEmail, userPassword, userFirstName, userLastName, userPhoneNumber, userType, userList, connection);
                        if (registerCheck) {
                            System.out.println("Pomyślnie dokonano rejestracji.");
                        } else {
                            System.out.println("Rejestracja nie przebiegła prawidłowo. Spróbuj jeszcze raz.");
                        }
                    } else if (userInputInt == 0) { // Wyłączenie programu
                        System.out.println("Koniec działania programu");
                        running = false;
                    } else { // Podanie błędnej liczby
                        System.out.println("Podano błędną liczbę.");
                    }
                } else { // Gdy użytkownik jest zalogowany
                    Functions.showCategories(connection);
                    System.out.println("Wpisz nazwę kategorii, aby wyświetlić znajdujące się w niej produkty.");
                    System.out.println("Wpisz 'Koszyk', aby wyświetlić zawartość swojego koszyka");
                    System.out.println("Wpisz 'Historia', aby zobaczyć historię swoich zakupów");
                    System.out.println("Wpisz 'Wyloguj', aby się wylogować.");
                    userInputString = scanner.nextLine().toLowerCase();
                    // Wylogowanie zalogowanego użytkownika
                    if (userInputString.equals("wyloguj")) {
                        CartDAO.updateCart(userCart, connection);
                        loggedIn = false;
                        continue;
                    } else if (userInputString.equals("koszyk")) {
                        List<Product> userCartProducts = userCart.getProducts();
                        if (userCartProducts == null || userCartProducts.isEmpty()) {
                            System.out.println("\nTwój koszyk jest pusty.\n");
                            continue;
                        } else {
                            System.out.printf("\nTwój koszyk:\n(ID, Nazwa, Cena, Ilość)\n");
                            for (Product product : userCartProducts) {
                                System.out.printf("%d, %s, %.2f, %d\n", product.getId(), product.getName(), product.getPrice(), userCart.getAmounts().get(userCartProducts.indexOf(product)));
                            }
                            System.out.printf("\nWartość koszyka wynosi: %.2f\n\n", userCart.getPrice());
                        }
                        System.out.println("Wpisz odpowiednie wartości, aby wybrać akcję.");
                        System.out.println("Wpisz 1, aby zakupić wszystkie produkty w koszyku");
                        System.out.println("Wpisz 2, aby wyczyścić koszyk");
                        System.out.println("Wpisz 3, aby usunąć produkt z koszyka");
                        System.out.println("Wpisz 0, aby powrócić do głównego menu");
                        userInputInt = scanner.nextInt();
                        scanner.nextLine();
                        if (userInputInt == 1) {
                            System.out.println("Aktualna wartość Twojego koszyka to: " + userCart.getPrice() + ".");
                            System.out.println("Czy na pewno dokonać zakupu? ('Tak'/'Nie')");
                            String userAnswer = scanner.nextLine().toLowerCase();
                            if (userAnswer.equals("tak")) {
                                userCart.buy(connection);
                            } else if (userAnswer.equals("nie")) {
                                continue;
                            } else {
                                System.out.println("Nie rozpoznano wprowadzonej odpowiedzi: " + userAnswer + ". Powrót do głównego menu...");
                            }
                        } else if (userInputInt == 2) {
                            userCart.clearProducts();
                            System.out.println("Wyczyszczono koszyk.\n");
                        } else if (userInputInt == 3) {
                            System.out.println("Wpisz ID produktu, który chcesz usunąć.");
                            int productIDToRemove = scanner.nextInt();
                            Product productToRemove = getProductFromList(productIDToRemove, productList);
                            scanner.nextLine();
                            System.out.println("Wybrany produkt: " + productToRemove.getName());
                            System.out.println("Ilość w koszyku: " + userCart.getProductAmount(productToRemove));
                            System.out.println("Wpisz ilość produktu, którą chcesz usunąć z koszyka.");
                            int amountToRemove = scanner.nextInt();
                            scanner.nextLine();
                            userCart.removeProductFromCart(productToRemove, amountToRemove);
                            System.out.println("Nowa ilość produktu w koszyku: " + userCart.getProductAmount(productToRemove) + "\n");
                        } else if (userInputInt == 0) {
                            continue;
                        }
                    } else if (userInputString.equals("historia")) {
                        UserDAO.showUserOrderHistory(activeUser.getId(), connection);
                    } else {
                        Functions.showCategoryProducts(userInputString, productList);
                        System.out.println("Wpisz ID produktu aby wyświetlić jego opis");
                        userInputInt = scanner.nextInt();
                        int productId = userInputInt;
                        Product chosenProduct = getProductFromList(productId, productList);
                        scanner.nextLine();
                        Functions.showProductInfo(userInputInt, productList, connection);
                        System.out.println("Wciśnij 1, aby dodać produkt do koszyka.");
                        System.out.println("Wciśnij 2, aby dodać opinię o produkcie.");
                        System.out.println("Wciśnij 3 aby wyświetlić opinie o produkcie");
                        System.out.println("Wciśnij 0, aby wrócić do głównego menu.");
                        userInputInt = scanner.nextInt();
                        scanner.nextLine();
                        if (userInputInt == 1) {
                            System.out.println("Wprowadź ilość produktu, jaką chcesz dodać do koszyka.");
                            int amount = scanner.nextInt();
                            scanner.nextLine();
                            userCart.addProductToCart(chosenProduct, amount);
                        } else if (userInputInt == 2) {
                            System.out.println("Podaj na ile oceniasz produkt (od 1 do 10):");
                            userInputDouble = scanner.nextDouble();
                            scanner.nextLine();
                            System.out.println("Napisz co sądzisz o produkcie:");
                            userInputString = scanner.nextLine();
                            ReviewsDAO.addReview(activeUser, chosenProduct, userInputString, userInputDouble, connection);
                        } else if (userInputInt == 3) {
                            ReviewsDAO.showReviews(chosenProduct, connection);
                        } else if (userInputInt == 0) {
                            continue;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }
}
