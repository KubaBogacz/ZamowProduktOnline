package users;

public class Buyer extends User {
    private final String userType = "buyer";

    public Buyer() {
    }

    public Buyer(int id, String email, String password, String name, String surname, int telNumber) {
        super(id, email, password, name, surname, telNumber);
    }
}
