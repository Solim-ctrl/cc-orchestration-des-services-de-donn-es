package ehs.ms.basket.model;
import java.util.List;

public class Basket {
    private String user;
    private double totalAmount;
    private String status;
    private List<Item> items;

    public Basket(String user, double totalAmount, String status, List<Item> items) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (Item item : items) {
            total += item.getUnitPrice() * item.getQuantity() * (1 + item.getVat());
        }
        return total;
    }

    // Getters and setters...

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
