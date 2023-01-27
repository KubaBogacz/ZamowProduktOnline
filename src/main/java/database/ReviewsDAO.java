package database;

import app.Functions;
import products.Product;
import users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewsDAO {

    public static void addReview(User user, Product product, String commentReview, double ratingGiven) {
        String sql = "INSERT INTO zpo.reviews (comment, rating, product_id, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, commentReview);
            preparedStatement.setDouble(2, ratingGiven);
            preparedStatement.setInt(3, product.getId());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding review: " + e.getMessage());
        }
    }

    public static void showReviews(Product product) {
        String sql = "SELECT comment, rating  FROM zpo.reviews where product.id = " + product.getId() + ";";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rsProductReviews = preparedStatement.executeQuery();
            Functions.printResultSet(rsProductReviews);
        } catch (SQLException e) {
            System.out.println("Error showing reviews: " + e.getMessage());
        }
    }

    public static double avgReviewRating(Product product) {
        double avgRating = 0.0;
        String sql = "SELECT avg(rating)  FROM zpo.reviews where product.id = " + product.getId() + ";";
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rsProductReviews = preparedStatement.executeQuery();
            Functions.printResultSet(rsProductReviews);
            while (rsProductReviews.next()) {
                avgRating = rsProductReviews.getDouble(1);
            }
            return avgRating;

        } catch (SQLException e) {
            System.out.println("Error calculating average rating: " + e.getMessage());
        }
        return avgRating;
    }
}
