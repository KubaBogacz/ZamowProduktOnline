package database;

import app.Functions;
import products.Product;
import users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewsDAO {

    /***
     * Metoda dodająca recenzję dla produktu
     * @param user - użytkownik dodający recenzję
     * @param product - recenzowany produkt
     * @param commentReview - komentarz do recenzji
     * @param ratingGiven - ocena produktu
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void addReview(User user, Product product, String commentReview, double ratingGiven, Connection connection) throws SQLException {
        String sql = "SELECT count(user_id) FROM zpo.reviews WHERE user_id = " + user.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        int checkIfReviewExists = 0;
        while (resultSet.next()) {
            checkIfReviewExists = resultSet.getInt(1);
        }
        if (checkIfReviewExists == 1) {
            System.out.println("Już napisałeś opinię dla tego produktu");
        } else {
            sql = "INSERT INTO zpo.reviews (comment, rating, product_id, user_id) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, commentReview);
            preparedStatement.setDouble(2, ratingGiven);
            preparedStatement.setInt(3, product.getId());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        }
    }

    /***
     * Metoda wyświetlająca recenzje produktu
     * @param product - wybrany produkt
     * @param connection - połączenie z BD
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static void showReviews(Product product, Connection connection) throws SQLException {
        String sql = "SELECT comment, rating FROM zpo.reviews INNER JOIN products ON products.id = reviews.product_id WHERE reviews.product_id = " + product.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsProductReviews = preparedStatement.executeQuery();
        Functions.printResultSet(rsProductReviews);
    }

    /***
     * Metoda zwracająca średnią wartość ocen produktu
     * @param product - wybrany produkt
     * @param connection - połączenie z BD
     * @return double - średnia wartość ocen
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static double avgReviewRating(Product product, Connection connection) throws SQLException {
        double avgRating = 0.0;
        String sql = "SELECT avg(rating) FROM zpo.reviews INNER JOIN products ON products.id = reviews.product_id WHERE reviews.product_id = " + product.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsProductReviews = preparedStatement.executeQuery();
        while (rsProductReviews.next()) {
            avgRating = rsProductReviews.getDouble(1);
        }
        return avgRating;
    }
}
