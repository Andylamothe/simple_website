/**
 * Représente un item du menu McDonald's
 */
public class Item {
    private String name;
    private double price;
    private int stock;
    private String type; // "main", "snack", "drink"
    private String size; // pour drinks seulement
    
    public Item(String name, double price, int stock, String type) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
        this.size = null;
    }
    
    public Item(String name, double price, int stock, String type, String size) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
        this.size = size;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public String getType() {
        return type;
    }
    
    public String getSize() {
        return size;
    }
    
    public void addStock(int quantity) {
        if (quantity > 0) {
            this.stock += quantity;
        }
    }
    
    public void removeStock(int quantity) {
        if (quantity > 0 && this.stock >= quantity) {
            this.stock -= quantity;
        }
    }
    
    public boolean isInStock() {
        return stock > 0;
    }
}