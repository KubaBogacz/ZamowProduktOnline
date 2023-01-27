package products;

import database.CartDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cart {
    private int userId;
    private ArrayList<Product> products;
    private ArrayList<Integer> amounts;
    private double price = 0;

    public void Cart() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Integer> getAmounts() {
        return amounts;
    }

    public void setAmounts(ArrayList<Integer> amounts) {
        this.amounts = amounts;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addProductToCart(Product product, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        if (products.contains(product)) {
            int index = products.indexOf(product);
            amounts.set(index, amounts.get(index) + amount);
        } else {
            products.add(product);
            amounts.add(amount);
        }
        updatePrice();
    }

    public void removeProductFromCart(Product product, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        if (products.contains(product)) {
            int index = products.indexOf(product);
            int amountInCart = amounts.get(index);
            if (amountInCart <= amount) {
                amounts.remove(index);
                products.remove(index);
            } else if (amountInCart > amount) {
                amounts.set(index, amounts.get(index) - amount);
            }
            updatePrice();
        }
    }

    public void clearProducts() {
        products.clear();
        amounts.clear();
        price = 0.0;
    }

    private void updatePrice() {
        double price = 0.0;
        for (Product product : products) {
            price += product.getPrice() * amounts.get(products.indexOf(product));
        }
        setPrice(price);
    }

    public String productsToDBString() {
        String returnString = "";
        for (Product product : products) {
            if (products.indexOf(product) != 0) {
                returnString += ";";
            }
            returnString += product.getId();
        }
        return returnString;
    }

    public String amountsToDBString() {
        String returnString = "";
        for (int i=0;i<amounts.size();i++) {
            if (i != 0) {
                returnString += ";";
            }
            returnString += amounts.get(i);
        }
        return returnString;
    }

    public int getProductAmount(Product product) {
        if (products.contains(product)) {
            int index = products.indexOf(product);
            return amounts.get(index);
        }
        return 0;
    }

    public void buy(Connection connection) throws SQLException {
        CartDAO.buyCart(this, connection);
    }
}
