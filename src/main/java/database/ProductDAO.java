package database;

import products.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ProductDAO {
    public void addProduct(Product product) {
        String sql = "INSERT INTO zpo.products (name, price) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }
    public void removeProduct(int productId) {
        String sql = "DELETE FROM zpo.products WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error removing product: " + e.getMessage());
        }
    }
}

