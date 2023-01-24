package users;

public class Buyer extends User {
    private final String userType = "buyer";

    public Buyer() {
    }

    public Buyer(String email, String password, String name, String surname, int telNumber) {
        super(email, password, name, surname, telNumber);
    }
}
