package ehs.ms.basket.model;
public class Item {
    private String gtin;
    private String label;
    private double unitPrice;
    private double vat; // Utilisation d'un taux de TVA sous forme de nombre (0.2 pour 20%)
    private int quantity;

    public Item(String gtin, String label, double unitPrice, double vat, int quantity) {
        this.gtin = gtin;
        this.label = label;
        this.unitPrice = unitPrice;
        this.vat = vat;
        this.quantity = quantity;
    }

    // Getters and setters...

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

