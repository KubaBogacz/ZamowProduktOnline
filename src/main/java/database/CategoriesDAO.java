package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoriesDAO {

    /***
     * Metoda importująca kategorie z BD i zwracająca ich listę
     * @param connection - połączenie z BD
     * @return - List<String> - lista nazw kategorii
     * @throws SQLException - jeśli połączenie z BD jes niepoprawne, bądź nie powiodło się wywołanie polecenia
     */
    public static List<String> importCategories(Connection connection) throws SQLException {
        String sql = "SELECT * FROM zpo.categories";
        PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ResultSet rsCategories = preparedStatement.executeQuery();
        List<String> categoriesList = new ArrayList<>();
        while (rsCategories.next()) {
            categoriesList.add(rsCategories.getString(1));
        }
        return categoriesList;
    }
}
