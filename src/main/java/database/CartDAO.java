package database;

import products.Cart;
import products.Product;

import java.sql.*;
import java.util.List;

public class CartDAO {

    public static void addCart(Cart cart, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.carts (user_id) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cart.getUserId());
    }

    public static void updateCart(Cart cart, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.carts (user_id, products, amounts, price) VALUES (?, ?, ?, ?)";
        assert connection != null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cart.getUserId());
        preparedStatement.setString(2, cart.productsToDBString());
        preparedStatement.setString(3, cart.amountsToDBString());
        preparedStatement.setDouble(4, cart.getPrice());
        preparedStatement.executeUpdate();
    }

    public static Cart importCart(int userId, List<Product> products, Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.carts WHERE user_id = " + userId + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsCart = preparedStatement.executeQuery();
        Cart cart = new Cart();
        while (rsCart.next()) {
            String[] productsIds = rsCart.getString(2).split(";");
            String[] amountsParts = rsCart.getString(3).split(";");
            // Dlugość powyższych Arrayów jest identyczna
            double price = rsCart.getDouble(4);

            cart.setUserId(userId);
            cart.setPrice(price);
            Product product = null;
            int productId;
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
        return cart;
    }

    public static void buyCart(Cart cart, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.orders (user_id, date, products, amounts, price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cart.getUserId());
        preparedStatement.setDate(2, Date.valueOf(java.time.LocalDate.now()));
        preparedStatement.setString(3, cart.productsToDBString());
        preparedStatement.setString(4, cart.amountsToDBString());
        preparedStatement.setDouble(5, cart.getPrice());
        preparedStatement.executeUpdate();
        cart.clearProducts();
    }
}