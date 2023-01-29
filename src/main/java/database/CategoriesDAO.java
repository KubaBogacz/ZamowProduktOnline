package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoriesDAO {

    public static List<String> importCategories(Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.categories";
        PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ResultSet rsCategories = preparedStatement.executeQuery();
        return createCategoriesList(rsCategories, connection);
    }
    private static List<String> createCategoriesList(ResultSet rsCategories, Connection connection) throws SQLException {
        List<String> categoriesList = new ArrayList<>();
        while (rsCategories.next()) {
            categoriesList.add(rsCategories.getString("name"));
        }
        return categoriesList;
    }

    public static String getCategoryName(int categoryId, Connection connection) throws SQLException {
        String sql = "SELECT name FROM zpo.categories WHERE id = " + categoryId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsName = preparedStatement.executeQuery();
        return rsName.getString(1);
    }

    public static int getCategoryId(int categoryName, Connection connection) throws SQLException {
        String sql = "SELECT id FROM zpo.categories WHERE name = " + categoryName;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rsId = preparedStatement.executeQuery();
        return rsId.getInt(1);
    }
}
