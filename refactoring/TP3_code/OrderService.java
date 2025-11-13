import java.util.ArrayList;

/**
 * Gère les opérations liées aux commandes
 */
public class OrderService {
    private InventoryManager inventoryManager;
    private int orderNumber;
    
    public OrderService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.orderNumber = 1;
    }
    
    public boolean validateCartStock(ShoppingCart cart) {
        ArrayList<CartItem> items = cart.getAllItems();
        boolean allItemsInStock = true;
        
        for (CartItem cartItem : items) {
            if (cartItem.isTrio()) {
                if (!inventoryManager.hasStockForTrio(
                        cartItem.getItem(), 
                        cartItem.getTrioSnack(), 
                        cartItem.getTrioDrink())) {
                    allItemsInStock = false;
                    System.out.println("ERREUR: Stock insuffisant pour " + cartItem.getDescription());
                }
            } else {
                if (!inventoryManager.hasStock(cartItem.getItem())) {
                    allItemsInStock = false;
                    System.out.println("ERREUR: Stock insuffisant pour " + cartItem.getItem().getName());
                }
            }
        }
        
        return allItemsInStock;
    }
    
    public void processOrder(ShoppingCart cart) {
        ArrayList<CartItem> items = cart.getAllItems();
        
        for (CartItem cartItem : items) {
            if (cartItem.isTrio()) {
                cartItem.getItem().removeStock(1);
                cartItem.getTrioSnack().removeStock(1);
                cartItem.getTrioDrink().removeStock(1);
            } else {
                cartItem.getItem().removeStock(1);
            }
        }
    }
    
    public int getNextOrderNumber() {
        return orderNumber++;
    }
}

