import java.util.ArrayList;

/**
 * Gère les opérations sur l'inventaire des items
 */
public class InventoryManager {
    private ArrayList<Item> inventory;
    
    public InventoryManager() {
        this.inventory = new ArrayList<>();
    }
    
    public void initializeDefaultInventory() {
        inventory.add(new Item("Big Mac", 6.99, 50, "main"));
        inventory.add(new Item("Quarter Pounder", 7.49, 40, "main"));
        inventory.add(new Item("McChicken", 5.99, 45, "main"));
        inventory.add(new Item("Frites", 3.49, 100, "snack"));
        inventory.add(new Item("Nuggets (6)", 4.99, 60, "snack"));
        inventory.add(new Item("Coca-Cola", 2.49, 80, "drink", "Medium"));
        inventory.add(new Item("Sprite", 2.49, 70, "drink", "Medium"));
        inventory.add(new Item("Jus d'orange", 2.99, 50, "drink", "Medium"));
    }
    
    public ArrayList<Item> getAllItems() {
        return new ArrayList<>(inventory);
    }
    
    public ArrayList<Item> getItemsByType(String type) {
        ArrayList<Item> filteredItems = new ArrayList<>();
        for (Item item : inventory) {
            if (item.getType().equals(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
    
    public Item getItemByIndex(int index) {
        if (index >= 0 && index < inventory.size()) {
            return inventory.get(index);
        }
        return null;
    }
    
    public Item findItemByName(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
    
    public void addItem(Item item) {
        inventory.add(item);
    }
    
    public void addStock(String itemName, int quantity) {
        Item item = findItemByName(itemName);
        if (item != null) {
            item.addStock(quantity);
        }
    }
    
    public boolean removeStock(String itemName, int quantity) {
        Item item = findItemByName(itemName);
        if (item != null && item.getStock() >= quantity) {
            item.removeStock(quantity);
            return true;
        }
        return false;
    }
    
    public boolean hasStock(Item item) {
        return item != null && item.getStock() > 0;
    }
    
    public boolean hasStockForTrio(Item main, Item snack, Item drink) {
        return hasStock(main) && hasStock(snack) && hasStock(drink);
    }
}

