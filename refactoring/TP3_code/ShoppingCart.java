import java.util.ArrayList;

/**
 * Gère le panier d'achat du client
 */
public class ShoppingCart {
    private ArrayList<CartItem> items;
    
    public ShoppingCart() {
        this.items = new ArrayList<>();
    }
    
    public void addItem(CartItem cartItem) {
        items.add(cartItem);
    }
    
    public CartItem removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }
    
    public void clear() {
        items.clear();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public int getSize() {
        return items.size();
    }
    
    public CartItem getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
    
    public ArrayList<CartItem> getAllItems() {
        return new ArrayList<>(items);
    }
    
    public double calculateTotal() {
        double total = 0.0;
        for (CartItem cartItem : items) {
            total += cartItem.getPrice();
        }
        return total;
    }
}

