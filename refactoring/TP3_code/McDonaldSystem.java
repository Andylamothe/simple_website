import java.util.ArrayList;

/**
 * Système principal de gestion McDonald's
 * Refactorisé pour séparer les responsabilités en différentes classes
 */
public class McDonaldSystem {
    private InventoryManager inventoryManager;
    private ShoppingCart shoppingCart;
    private MenuDisplay menuDisplay;
    private OrderService orderService;
    private InputHandler inputHandler;
    
    public McDonaldSystem() {
        this.inventoryManager = new InventoryManager();
        this.shoppingCart = new ShoppingCart();
        this.menuDisplay = new MenuDisplay();
        this.orderService = new OrderService(inventoryManager);
        this.inputHandler = new InputHandler();
    }
    
    public static void main(String[] args) {
        McDonaldSystem system = new McDonaldSystem();
        system.initialize();
        system.run();
    }
    
    private void initialize() {
        inventoryManager.initializeDefaultInventory();
        System.out.println("=== MCDONALDS ===");
    }
    
    private void run() {
        boolean isRunning = true;
        
        while (isRunning) {
            menuDisplay.displayMainMenu();
            int userChoice = inputHandler.readInt();
            
            switch (userChoice) {
                case 1:
                    handleClientMode();
                    break;
                case 2:
                    handleInventoryMode();
                    break;
                case 3:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
        
        inputHandler.close();
    }
    
    private void handleClientMode() {
        String clientName = inputHandler.readStringWithPrompt("Nom: ");
        System.out.println("Bienvenue " + clientName);
        
        shoppingCart.clear();
        
        boolean isInClientMode = true;
        while (isInClientMode) {
            menuDisplay.displayClientMenu();
            int clientChoice = inputHandler.readInt();
            
            switch (clientChoice) {
                case 1:
                    displayMenu();
                    break;
                case 2:
                    addTrioToCart();
                    break;
                case 3:
                    addItemToCart();
                    break;
                case 4:
                    displayCart();
                    break;
                case 5:
                    removeItemFromCart();
                    break;
                case 6:
                    processOrder();
                    break;
                case 7:
                    isInClientMode = false;
                    shoppingCart.clear();
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
    private void displayMenu() {
        ArrayList<Item> allItems = inventoryManager.getAllItems();
        menuDisplay.displayMenu(allItems);
    }
    
    private void addTrioToCart() {
        ArrayList<Item> mainDishes = inventoryManager.getItemsByType("main");
        ArrayList<Item> snacks = inventoryManager.getItemsByType("snack");
        ArrayList<Item> drinks = inventoryManager.getItemsByType("drink");
        
        menuDisplay.displayItemsByCategory("Plats principaux", mainDishes);
        int mainChoice = inputHandler.readIntWithPrompt("Choix: ") - 1;
        
        menuDisplay.displayItemsByCategory("Accompagnements", snacks);
        int snackChoice = inputHandler.readIntWithPrompt("Choix: ") - 1;
        
        menuDisplay.displayItemsByCategory("Boissons", drinks);
        int drinkChoice = inputHandler.readIntWithPrompt("Choix: ") - 1;
        
        if (isValidTrioSelection(mainDishes, snacks, drinks, mainChoice, snackChoice, drinkChoice)) {
            Item selectedMain = mainDishes.get(mainChoice);
            Item selectedSnack = snacks.get(snackChoice);
            Item selectedDrink = drinks.get(drinkChoice);
            
            if (inventoryManager.hasStockForTrio(selectedMain, selectedSnack, selectedDrink)) {
                CartItem trio = new CartItem(selectedMain, selectedSnack, selectedDrink);
                shoppingCart.addItem(trio);
                System.out.println("✓ Trio ajouté au panier!");
            } else {
                System.out.println("ERREUR: Stock insuffisant pour ce trio!");
            }
        } else {
            System.out.println("ERREUR: Choix invalide");
        }
    }
    
    private boolean isValidTrioSelection(ArrayList<Item> mainDishes, ArrayList<Item> snacks, 
                                        ArrayList<Item> drinks, int mainIndex, int snackIndex, int drinkIndex) {
        return mainIndex >= 0 && mainIndex < mainDishes.size() &&
               snackIndex >= 0 && snackIndex < snacks.size() &&
               drinkIndex >= 0 && drinkIndex < drinks.size();
    }
    
    private void addItemToCart() {
        ArrayList<Item> allItems = inventoryManager.getAllItems();
        menuDisplay.displayMenu(allItems);
        
        int itemChoice = inputHandler.readIntWithPrompt("Choix: ") - 1;
        Item selectedItem = inventoryManager.getItemByIndex(itemChoice);
        
        if (selectedItem != null) {
            if (inventoryManager.hasStock(selectedItem)) {
                CartItem cartItem = new CartItem(selectedItem);
                shoppingCart.addItem(cartItem);
                System.out.println("✓ " + selectedItem.getName() + " ajouté au panier!");
            } else {
                System.out.println("ERREUR: Plus de stock pour " + selectedItem.getName());
            }
        } else {
            System.out.println("ERREUR: Choix invalide");
        }
    }
    
    private void displayCart() {
        menuDisplay.displayCart(shoppingCart);
    }
    
    private void removeItemFromCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("\nPanier vide!");
        } else {
            menuDisplay.displayCart(shoppingCart);
            int removeChoice = inputHandler.readIntWithPrompt(
                "\nNuméro de l'item à retirer (0 pour annuler): ");
            
            if (removeChoice > 0 && removeChoice <= shoppingCart.getSize()) {
                CartItem removedItem = shoppingCart.removeItem(removeChoice - 1);
                System.out.println("✓ " + removedItem.getDescription() + " retiré du panier!");
            } else if (removeChoice != 0) {
                System.out.println("ERREUR: Choix invalide");
            }
        }
    }
    
    private void processOrder() {
        if (shoppingCart.isEmpty()) {
            System.out.println("\nPanier vide! Ajoutez des items d'abord.");
            return;
        }
        
        if (orderService.validateCartStock(shoppingCart)) {
            orderService.processOrder(shoppingCart);
            
            int orderNumber = orderService.getNextOrderNumber();
            menuDisplay.displayReceipt(orderNumber, shoppingCart);
            
            shoppingCart.clear();
            System.out.println("\n✓ Commande passée avec succès!");
        }
    }
    
    private void handleInventoryMode() {
        boolean isInInventoryMode = true;
        
        while (isInInventoryMode) {
            menuDisplay.displayInventoryMenu();
            int inventoryChoice = inputHandler.readInt();
            
            switch (inventoryChoice) {
                case 1:
                    displayInventory();
                    break;
                case 2:
                    addStockToItem();
                    break;
                case 3:
                    removeStockFromItem();
                    break;
                case 4:
                    addNewItem();
                    break;
                case 5:
                    isInInventoryMode = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
    private void displayInventory() {
        ArrayList<Item> allItems = inventoryManager.getAllItems();
        menuDisplay.displayInventory(allItems);
    }
    
    private void addStockToItem() {
        String itemName = inputHandler.readStringWithPrompt("Nom de l'item: ");
        Item item = inventoryManager.findItemByName(itemName);
        
        if (item != null) {
            int quantity = inputHandler.readIntWithPrompt("Quantité à ajouter: ");
            inventoryManager.addStock(itemName, quantity);
            System.out.println("Stock ajouté!");
        } else {
            System.out.println("Item non trouvé");
        }
    }
    
    private void removeStockFromItem() {
        String itemName = inputHandler.readStringWithPrompt("Nom de l'item: ");
        Item item = inventoryManager.findItemByName(itemName);
        
        if (item != null) {
            int quantity = inputHandler.readIntWithPrompt("Quantité à retirer: ");
            boolean success = inventoryManager.removeStock(itemName, quantity);
            
            if (success) {
                System.out.println("Stock retiré!");
            } else {
                System.out.println("ERREUR: Pas assez de stock!");
            }
        } else {
            System.out.println("Item non trouvé");
        }
    }
    
    private void addNewItem() {
        String itemName = inputHandler.readStringWithPrompt("Nom: ");
        double itemPrice = inputHandler.readDoubleWithPrompt("Prix: ");
        int initialStock = inputHandler.readIntWithPrompt("Stock initial: ");
        String itemType = inputHandler.readStringWithPrompt("Type (main/snack/drink): ");
        
        Item newItem;
        if (itemType.equals("drink")) {
            String drinkSize = inputHandler.readStringWithPrompt("Taille: ");
            newItem = new Item(itemName, itemPrice, initialStock, itemType, drinkSize);
        } else {
            newItem = new Item(itemName, itemPrice, initialStock, itemType);
        }
        
        inventoryManager.addItem(newItem);
        System.out.println("Item ajouté!");
    }
}
