import java.util.ArrayList;

/**
 * Gère l'affichage des menus et des informations
 */
public class MenuDisplay {
    
    public void displayMainMenu() {
        System.out.println("\n1. Mode Client");
        System.out.println("2. Mode Inventaire");
        System.out.println("3. Quitter");
        System.out.print("Choix: ");
    }
    
    public void displayClientMenu() {
        System.out.println("\n1. Voir menu");
        System.out.println("2. Ajouter TRIO au panier");
        System.out.println("3. Ajouter item au panier");
        System.out.println("4. Voir panier");
        System.out.println("5. Retirer du panier");
        System.out.println("6. Passer commande");
        System.out.println("7. Retour");
        System.out.print("Choix: ");
    }
    
    public void displayInventoryMenu() {
        System.out.println("\n=== INVENTAIRE ===");
        System.out.println("1. Afficher inventaire");
        System.out.println("2. Ajouter stock");
        System.out.println("3. Retirer stock");
        System.out.println("4. Ajouter nouvel item");
        System.out.println("5. Retour");
        System.out.print("Choix: ");
    }
    
    public void displayMenu(ArrayList<Item> inventory) {
        System.out.println("\n=== MENU ===");
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - " + 
                             item.getPrice() + "$ (stock: " + item.getStock() + ")");
        }
    }
    
    public void displayMenuWithIndex(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - " + item.getPrice() + "$");
        }
    }
    
    public void displayCart(ShoppingCart cart) {
        if (cart.isEmpty()) {
            System.out.println("\nPanier vide!");
        } else {
            System.out.println("\n=== VOTRE PANIER ===");
            ArrayList<CartItem> items = cart.getAllItems();
            for (int i = 0; i < items.size(); i++) {
                CartItem cartItem = items.get(i);
                System.out.printf("%d. %s - %.2f$\n", 
                                (i + 1), cartItem.getDescription(), cartItem.getPrice());
            }
            System.out.println("--------------------");
            System.out.printf("TOTAL: %.2f$\n", cart.calculateTotal());
        }
    }
    
    public void displayInventory(ArrayList<Item> inventory) {
        System.out.println("\n--- STOCK ACTUEL ---");
        for (Item item : inventory) {
            System.out.println(item.getName() + ": " + item.getStock() + 
                             " unités (" + item.getPrice() + "$)");
        }
    }
    
    public void displayReceipt(int orderNumber, ShoppingCart cart) {
        System.out.println("\n========= RECU =========");
        System.out.println("Commande #" + orderNumber);
        ArrayList<CartItem> items = cart.getAllItems();
        for (CartItem cartItem : items) {
            System.out.printf("%s - %.2f$\n", cartItem.getDescription(), cartItem.getPrice());
        }
        System.out.println("------------------------");
        System.out.printf("TOTAL: %.2f$\n", cart.calculateTotal());
        System.out.println("========================");
    }
    
    public void displayItemsByCategory(String categoryName, ArrayList<Item> items) {
        System.out.println("\n" + categoryName + ":");
        displayMenuWithIndex(items);
    }
}

