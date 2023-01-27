package users;

public class Buyer extends User {
    private final String userType = "buyer";

    public String getUserType() {
        return userType;
    }

    public Buyer() {
    }
}
