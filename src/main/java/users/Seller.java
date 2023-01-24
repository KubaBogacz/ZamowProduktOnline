package users;

public class Seller extends User {
    private final String userType = "buyer";

    public Seller() {
    }

    public Seller(String email, String password, String name, String surname, int telNumber) {
        super(email, password, name, surname, telNumber);
    }
}
