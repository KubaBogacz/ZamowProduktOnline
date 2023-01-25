package database;

import products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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

    // Funkcja zwracajaca ArrayList Products nie przyjmujac zadnych argumentow - do wywolania na starcie programu
    public static List<Product> importProducts() {
        String sql = "SELECT * FROM zpo.products";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rsProducts = preparedStatement.executeQuery();
                return createProductList(rsProducts);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<Product> createProductList(ResultSet rsProducts) throws SQLException {
        List<Product> productList = new ArrayList<>();

        while (rsProducts.next()) {
            productList.add(new Product(rsProducts.getInt(1), rsProducts.getString(2), rsProducts.getDouble(3), rsProducts.getString(4), rsProducts.getString(5)));
        }
        return productList;
    }
}

