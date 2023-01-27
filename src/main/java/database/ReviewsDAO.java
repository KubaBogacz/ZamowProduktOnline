package database;

import app.Functions;
import products.Product;
import users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewsDAO {

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

    public static void showReviews(Product product, Connection connection) throws SQLException {
        String sql = "SELECT comment, rating FROM zpo.reviews INNER JOIN products ON products.id = reviews.product_id WHERE reviews.product_id = " + product.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsProductReviews = preparedStatement.executeQuery();
        Functions.printResultSet(rsProductReviews);

    }

    public static double avgReviewRating(Product product, Connection connection) throws SQLException {
        double avgRating = 0.0;
        String sql = "SELECT avg(rating) FROM zpo.reviews INNER JOIN products ON products.id = reviews.product_id WHERE reviews.product_id = " + product.getId() + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsProductReviews = preparedStatement.executeQuery();
        Functions.printResultSet(rsProductReviews);
        while (rsProductReviews.next()) {
            avgRating = rsProductReviews.getDouble(1);
        }
        return avgRating;
    }
}
