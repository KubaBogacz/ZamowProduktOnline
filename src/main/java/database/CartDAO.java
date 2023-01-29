package database;

import products.Cart;
import products.Product;

import java.sql.*;
import java.util.List;

public class CartDAO {

    /***
     * Metoda dodająca nowy wózek do BD
     * @param cart - dodawany wózek
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void addCart(Cart cart, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.carts (user_id) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cart.getUserId());
        preparedStatement.executeUpdate();
    }

    /***
     * Metoda aktualizująca wózek w BD na aktualny stan wózka
     * @param cart - wózek
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void updateCart(Cart cart, Connection connection) throws SQLException {
        String sql;
        if (cart.getPrice() == 0) {
            sql = "UPDATE zpo.carts SET products = " + null + ", amounts = " + null + ", price = " + null + " WHERE user_id = " + cart.getUserId() + ";";
        } else {
            sql = "UPDATE zpo.carts SET products = " + cart.productsToDBString() +
                    ", amounts = " + cart.amountsToDBString() + ", price = " + cart.getPrice() +
                    " WHERE user_id = " + cart.getUserId() + ";";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    /***
     * Metoda importująca wózek danego użytkownika z BD
     * @param userId - ID użytkownika
     * @param products - zaimportowana z BD lista produktów
     * @param connection - połączenie z BD
     * @return Cart - wózek użytkownika
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static Cart importCart(int userId, List<Product> products, Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.carts WHERE user_id = " + userId + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsCart = preparedStatement.executeQuery();
        Cart cart = new Cart();
        while (rsCart.next()) {
            String[] productsIds = null;
            String[] amountsParts = null;
            if (rsCart.getString(2) != null) {
                 productsIds = rsCart.getString(2).split(";");
                 amountsParts= rsCart.getString(3).split(";");
            }
            // Dlugość powyższych Arrayów jest identyczna
            double price = rsCart.getDouble(4);

            cart.setUserId(userId);
            cart.setPrice(price);
            Product product = null;
            int productId;
            if (productsIds != null) {
                for (int i = 0; i < productsIds.length; i++) {
                    productId = Integer.parseInt(productsIds[i]);
                    for (int n = 0; n < products.size(); n++) {
                        product = products.get(n);
                        if (product.getId() == productId) {
                            break;
                        }
                    }
                    cart.addProductToCart(product, Integer.parseInt(amountsParts[i]));
                }
            }
        }
        return cart;
    }

    /***
     * Metoda obsługująca zakup produktów z koszyka.
     * @param cart - wózek
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void buyCart(Cart cart, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.orders (user_id, date, products, amounts, price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cart.getUserId());
        preparedStatement.setDate(2, Date.valueOf(java.time.LocalDate.now()));
        preparedStatement.setString(3, cart.productsToDBString());
        preparedStatement.setString(4, cart.amountsToDBString());
        preparedStatement.setDouble(5, cart.getPrice());
        preparedStatement.executeUpdate();
        // Aktualizowanie informacji o liczbie sprzedanych sztuk produktu w BD.
        for (Product product : cart.getProducts()) {
            ProductDAO.addToSoldAmount(product, cart.getProductAmount(product), connection);
        }
        cart.clearProducts();
    }
}