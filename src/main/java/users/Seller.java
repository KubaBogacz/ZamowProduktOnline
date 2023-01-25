package users;

public class Seller extends User {
    private final String userType = "buyer";

    public Seller() {
    }

    public Seller(int id, String email, String password, String name, String surname, int telNumber) {
        super(id, email, password, name, surname, telNumber);
    }
}
