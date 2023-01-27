package users;

public class Seller extends User {
    private final String userType = "seller";

    public String getUserType() {
        return userType;
    }

    public Seller() {
    }
}
