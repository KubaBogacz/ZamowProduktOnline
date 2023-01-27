import app.ZPOApp;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            ZPOApp.run();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

