package database;

import products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {

    // Funkcja dodająca produkty do BD
    public void addProduct(Product product, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.products (id, name, price, description, category) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setDouble(3, product.getPrice());
        preparedStatement.setString(4, product.getDescription());
        preparedStatement.setString(5, product.getCategory());
        preparedStatement.executeUpdate();
    }

    // Funkcja usuwająca produkty z BD
    public void removeProduct(int productId, Connection connection) throws SQLException {
        String sql = "DELETE FROM zpo.products WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, productId);
        preparedStatement.executeUpdate();
    }

    // Funkcja aktualizująca produkt w BD
    public void updateProduct(Product product, Connection connection) throws SQLException {
        String sql = "UPDATE zpo.products SET name = ?, price = ?, description = ?, category = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setString(4, product.getCategory());
        preparedStatement.setInt(5, product.getId());
        preparedStatement.executeUpdate();
    }

    // Funkcja zwracajaca ArrayList Products nie przyjmujac zadnych argumentow - do wywolania na starcie programu
    public static List<Product> importProducts(Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.products";
        PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ResultSet rsProducts = preparedStatement.executeQuery();
        return createProductList(rsProducts, connection);
    }
    private static List<Product> createProductList(ResultSet rsProducts, Connection connection) throws SQLException {
        List<Product> productList = new ArrayList<>();
        while (rsProducts.next()) {
            int categoryId = rsProducts.getInt(5);
            String categoryName = getCategoryName(categoryId, connection);
            productList.add(new Product(rsProducts.getInt(1), rsProducts.getString(2), rsProducts.getDouble(3), rsProducts.getString(4), categoryName));
        }
        return productList;
    }

    private static String getCategoryName(int categoryId, Connection connection) throws SQLException {
        String sql = "SELECT name FROM zpo.categories WHERE id = " + categoryId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsName = preparedStatement.executeQuery();
        while (rsName.next()) {
            return rsName.getString(1);
        }
        return null;
    }
}

