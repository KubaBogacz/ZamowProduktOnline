package database;

import products.Cart;
import products.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDAO {
    public void saveCartToDB(Cart cart) {
        String sql = "INSERT INTO zpo.carts (user_id, products, amounts, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cart.getUserId());
            preparedStatement.setString(2, cart.productsToDBString());
            preparedStatement.setString(3, cart.amountsToDBString());
            preparedStatement.setDouble(4, cart.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public Cart readCartFromDB(int userId, ArrayList<Product> products) {
        String sql = "SELECT * FROM zpo.carts WHERE user_id = " + userId + ";";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
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
                    for (int j = 0; j < Integer.parseInt(amountsParts[i]); j++) {
                        cart.addProductToCart(product);
                    }
                }
            }
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}