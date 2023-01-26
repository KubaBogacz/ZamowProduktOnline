import database.ProductDAO;
import database.UserDAO;
import products.Product;
import users.User;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        List<User> userList = UserDAO.importUsers();
        for (User user : userList) {
            System.out.println(user.getEmail());
        }
        List<Product> productList = ProductDAO.importProducts();
        for (Product product : productList) {
            System.out.println(product.getCategory());
        }

    }
}

