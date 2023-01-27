import database.ProductDAO;
import database.UserDAO;
import products.Product;
import users.User;

import app.Functions;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        List<User> userList = UserDAO.importUsers();
        for (User user : userList) {
            System.out.println(user.getEmail());
            System.out.println(user.getPassword());
            System.out.println(user.getId());
        }

        List<Product> productList = ProductDAO.importProducts();

        Functions functions = new Functions();
        Scanner scanner = new Scanner(System.in);

        String userInputString;
        int userInputInt;

        boolean running = true;
        boolean loggedIn = false;

        // Pętla gdy użytkownik nie jest zalogowany
        while (!loggedIn && running) {
            System.out.println("Wciśnij 1 aby się zalogować, 2 aby utworzyć nowe konto, 0 aby zakończyć działanie programu.");
            userInputInt = scanner.nextInt();
            scanner.nextLine();
            // Logowanie użytkownika
            if (userInputInt == 1) {
                System.out.println("Podaj E-Mail:");
                String userEmail = scanner.nextLine();
                System.out.println("Podaj hasło:");
                String userPassword = scanner.nextLine();
                User activeUser = functions.login(userEmail, userPassword, userList);
                if (activeUser != null) {
                    loggedIn = true;
                }
            } // Rejestracja użytkownika
            else if (userInputInt == 2) {
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
                if (Objects.equals(ans.toLowerCase(), "yes")) {
                    userType = "seller";
                } else if (!Objects.equals(ans, "no")) {
                    System.out.println("Wprowadzono nieprawidłową odpowiedź. Ustawiono wartość domyślną na: Kupujący.");
                }
                int lastID = userList.get(userList.size() - 1).getId() + 1;
                boolean registerCheck = functions.createAccount(lastID, userEmail, userPassword, userFirstName, userLastName, userPhoneNumber, userType, userList);
                if (registerCheck) {
                    System.out.println("Pomyślnie dokonano rejestracji.");
                } else if (registerCheck) {
                    System.out.println("Rejestracja nie przebiegła prawidłowo. Spróbuj jeszcze raz.");
                }
            } // Wyłączenie programu
            else if (userInputInt == 0) {
                System.out.println("Koniec działania programu");
                running = false;
            } // Podanie błędnej liczby
            else {
                System.out.println("Podano błędną liczbę.");
            }
            // Pętla, gdy użytkownik jest zalogowany
            while (loggedIn && running) {
                Functions.showCategories();
                System.out.println("Wpisz nazwę kategorii aby wyświetlić znajdujące się w niej produkty.");
                System.out.println("Wpisz 'Koszyk' aby wyświetlić zawartość swojego koszyka");
                System.out.println("Wpisz 'Historia' aby zobaczyć historię swoich zakupów");
                System.out.println("Wpisz 'Wyloguj' aby się wylogować.");
                userInputString = scanner.nextLine();
                // Wylogowanie zalogowanego użytkownika
                if (userInputString.equals("Wyloguj")) {
                    loggedIn = false;
                    continue;
                } else if (userInputString.equals("Koszyk")) {
                    // Tutaj trzeba zrobić koszyk, który będzie można clearować lub kupić
                    System.out.println("Brak implementacji koszyka");
                } else if (userInputString.equals("Historia")) {
                    // Tutaj należy wyświetlić historię zakupów użytkownika
                    System.out.println("Brak implementacji historii zakupów");
                } else {
                    Functions.showCategoryProducts(userInputString, productList);
                    System.out.println("Wpisz nazwę produktu aby wyświetlić jego opis");
                    userInputString = scanner.nextLine();
                    Functions.showProductInfo(userInputString, productList);
                    System.out.println("Wciśnij 1 aby dodać produkt do koszyka.");
                    System.out.println("Wciśnij 2 aby dodać opinię o produkcie.");
                    System.out.println("Wciśnij 0 aby wrócić do głównego menu.");
                    userInputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (userInputInt == 1) {
                        System.out.println("Brak opcji dodawania rzeczy do koszyka");
                    } else if (userInputInt == 2) {
                        System.out.println("Brak opcji dodawania opinii o produkcie");
                    } else if (userInputInt == 0) {
                        continue;
                    }
                }
            }
        }

    }
}

