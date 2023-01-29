package products;

import database.CartDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int userId;
    private List<Product> products = new ArrayList<Product>();
    private List<Integer> amounts = new ArrayList<Integer>();
    private double price = 0;

    public void Cart() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Integer> amounts) {
        this.amounts = amounts;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /***
     * Metoda obsługująca dodawanie produktu (w dowolnej ilości) do koszyka
     * @param product - produkt
     * @param amount - ilość
     */
    public void addProductToCart(Product product, int amount) {
        if (amount < 0) { // Obsługa ujemnej wartości wprowadzonej przez użytkownika
            amount = 0;
        }
        if (products.contains(product)) { // Jeśli produkt jest już w liście, nie będzie dodawany, a zmieni się jego liczność
            int index = products.indexOf(product);
            amounts.set(index, amounts.get(index) + amount);
        } else { // W przeciwnym wypadku, produkt zostanie dopisany do listy
            products.add(product);
            amounts.add(amount);
        }
        updatePrice(); // Aktualizacja wartości koszyka
    }

    /***
     * Metoda obsługująca usuwanie produktu z koszyka
     * @param product - usuwany produkt
     * @param amount - usuwana liczba produktu
     */
    public void removeProductFromCart(Product product, int amount) {
        if (amount < 0) { // Obsługa ujemnej wartości wprowadzonej przez użytkownika
            amount = 0;
        }
        if (products.contains(product)) { // Tylko jeśli koszyk zawiera produkt
            int index = products.indexOf(product);
            int amountInCart = amounts.get(index);
            if (amountInCart <= amount) { // Jeśli ilość produktu w koszyku jest <= wprowadzonej usuwanej ilości
                amounts.remove(index); // Usuwa się produkt z koszyka, by nie pozwolić na ujemną ilość produktu w koszyku
                products.remove(index);
            } else { // W przeciwnym wypadku, usuwa się określoną ilość produktu z koszyka
                amounts.set(index, amounts.get(index) - amount);
            }
            updatePrice(); // Aktualizacja wartości koszyka
        }
    }

    /***
     * Metoda czyszcząca koszyk
     */
    public void clearProducts() {
        products.clear();
        amounts.clear();
        price = 0.0;
    }

    /***
     * Metoda aktualizująca wartość koszyka
     */
    private void updatePrice() {
        double price = 0.0;
        for (Product product : products) {
            price += product.getPrice() * amounts.get(products.indexOf(product));
        }
        setPrice(price);
    }

    /***
     * Metoda tworząca i zwracająca String z ID produktów z koszyka, który jest przechowywany w BD
     * @return String - stworzony String
     */
    public String productsToDBString() {
        String returnString = "";
        for (Product product : products) {
            if (products.indexOf(product) != 0) {
                returnString += ";";
            }
            returnString += product.getId();
        }
        return returnString;
    }

    /***
     * Metoda tworząca i zwracająca String z ilościami produktów z koszyka, który jest przechowywany w BD
     * @return String - stworzony String
     */
    public String amountsToDBString() {
        String returnString = "";
        for (int i=0;i<amounts.size();i++) {
            if (i != 0) {
                returnString += ";";
            }
            returnString += amounts.get(i);
        }
        return returnString;
    }

    /***
     * Metoda zwracająca ilość danego produktu w koszyku
     * @param product - zliczany produkt
     * @return int - ilość produktu w koszyku
     */
    public int getProductAmount(Product product) {
        if (products.contains(product)) {
            int index = products.indexOf(product);
            return amounts.get(index);
        }
        return 0;
    }

    /***
     * Metoda obsługująca zakup całego koszyka
     * @param connection - połączenie z BD
     * @throws SQLException - - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public void buy(Connection connection) throws SQLException {
        CartDAO.buyCart(this, connection);
    }
}
