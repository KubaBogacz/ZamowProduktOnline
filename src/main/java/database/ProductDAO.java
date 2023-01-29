package database;

import products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {

    /***
     * Metoda dodająca nowy produkt do BD
     * @param product - produkt do dodania
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void addProduct(Product product, Connection connection) throws SQLException {
        String sql = "INSERT INTO zpo.products(name, price, description, category, sold_amount) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setString(4, product.getCategory());
        preparedStatement.setInt(5, 0);
        preparedStatement.executeUpdate();
    }

    /***
     * Metoda zwracająca listę produktów wczytanych z BD
     * @param connection - połączenie z BD
     * @return List<Product> - lista produktów
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static List<Product> importProducts(Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.products";
        PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ResultSet rsProducts = preparedStatement.executeQuery();
        return createProductList(rsProducts);
    }

    /***
     * Metoda tworząca listę produktów z dostarczonego ResultSet z BD
     * @return List<Product> - lista produktów
     */
    private static List<Product> createProductList(ResultSet rsProducts) throws SQLException {
        List<Product> productList = new ArrayList<>();
        while (rsProducts.next()) {
            productList.add(new Product(rsProducts.getInt(1), rsProducts.getString(2),
                    rsProducts.getDouble(3), rsProducts.getString(4), rsProducts.getString(5)));
        }
        return productList;
    }

    /***
     * Metoda aktualizująca liczbę sprzedanych sztuk produktu
     * @param product - wybrany produkt
     * @param amount - liczba nowo sprzedanych sztuk
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void addToSoldAmount(Product product, int amount, Connection connection) throws SQLException {
        String sql = "UPDATE zpo.products SET sold_amount = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(2, product.getId());
        preparedStatement.setInt(1, amount);
        preparedStatement.executeUpdate();
    }

    /***
     * Metoda zwracająca informację o liczbie sprzedanych sztuk produktu
     * @param product - wybrany produkt
     * @param connection - połączenie z BD
     * @return int - liczba sprzedanych sztuk produktu
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static int getSoldAmount(Product product, Connection connection) throws SQLException {
        String sql = "SELECT sold_amount FROM zpo.products WHERE id = " + product.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsSoldAmount = preparedStatement.executeQuery();
        int soldAmt = 0;
        while (rsSoldAmount.next()) {
            soldAmt = rsSoldAmount.getInt(1);
        }
        return soldAmt;
    }
}

