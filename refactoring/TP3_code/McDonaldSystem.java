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
            try {
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
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Veuillez réessayer.");
            }
        }
        
        inputHandler.close();
    }
    
    private void handleClientMode() {
        try {
            String clientName = inputHandler.readStringWithPrompt("Nom: ");
            System.out.println("Bienvenue " + clientName);
            
            shoppingCart.clear();
            
            boolean isInClientMode = true;
            while (isInClientMode) {
                try {
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
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Veuillez réessayer.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Retour au menu principal.");
        }
    }
    
    private void displayMenu() {
        ArrayList<Item> allItems = inventoryManager.getAllItems();
        menuDisplay.displayMenu(allItems);
    }
    
    private void addTrioToCart() {
        try {
            ArrayList<Item> mainDishes = inventoryManager.getItemsByType("main");
            ArrayList<Item> snacks = inventoryManager.getItemsByType("snack");
            ArrayList<Item> drinks = inventoryManager.getItemsByType("drink");
            
            if (mainDishes.isEmpty() || snacks.isEmpty() || drinks.isEmpty()) {
                System.out.println("ERREUR: Certaines catégories sont vides. Impossible de créer un trio.");
                return;
            }
            
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
                System.out.println("ERREUR: Choix invalide. Veuillez sélectionner un numéro valide.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private boolean isValidTrioSelection(ArrayList<Item> mainDishes, ArrayList<Item> snacks, 
                                        ArrayList<Item> drinks, int mainIndex, int snackIndex, int drinkIndex) {
        return mainIndex >= 0 && mainIndex < mainDishes.size() &&
               snackIndex >= 0 && snackIndex < snacks.size() &&
               drinkIndex >= 0 && drinkIndex < drinks.size();
    }
    
    private void addItemToCart() {
        try {
            ArrayList<Item> allItems = inventoryManager.getAllItems();
            if (allItems.isEmpty()) {
                System.out.println("ERREUR: L'inventaire est vide.");
                return;
            }
            
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
                System.out.println("ERREUR: Choix invalide. Veuillez sélectionner un numéro valide.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void displayCart() {
        menuDisplay.displayCart(shoppingCart);
    }
    
    private void removeItemFromCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("\nPanier vide!");
        } else {
            try {
                menuDisplay.displayCart(shoppingCart);
                int removeChoice = inputHandler.readIntWithPrompt(
                    "\nNuméro de l'item à retirer (0 pour annuler): ");
                
                if (removeChoice > 0 && removeChoice <= shoppingCart.getSize()) {
                    CartItem removedItem = shoppingCart.removeItem(removeChoice - 1);
                    if (removedItem != null) {
                        System.out.println("✓ " + removedItem.getDescription() + " retiré du panier!");
                    } else {
                        System.out.println("ERREUR: Impossible de retirer l'item.");
                    }
                } else if (removeChoice != 0) {
                    System.out.println("ERREUR: Choix invalide. Veuillez sélectionner un numéro valide.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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
            try {
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
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Veuillez réessayer.");
            }
        }
    }
    
    private void displayInventory() {
        ArrayList<Item> allItems = inventoryManager.getAllItems();
        menuDisplay.displayInventory(allItems);
    }
    
    private void addStockToItem() {
        try {
            String itemName = inputHandler.readStringWithPrompt("Nom de l'item: ");
            if (itemName == null || itemName.trim().isEmpty()) {
                System.out.println("ERREUR: Le nom de l'item ne peut pas être vide.");
                return;
            }
            
            Item item = inventoryManager.findItemByName(itemName);
            if (item != null) {
                int quantity = inputHandler.readIntWithPrompt("Quantité à ajouter: ");
                if (quantity > 0) {
                    inventoryManager.addStock(itemName, quantity);
                    System.out.println("Stock ajouté!");
                } else {
                    System.out.println("ERREUR: La quantité doit être positive.");
                }
            } else {
                System.out.println("ERREUR: Item non trouvé: " + itemName);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void removeStockFromItem() {
        try {
            String itemName = inputHandler.readStringWithPrompt("Nom de l'item: ");
            if (itemName == null || itemName.trim().isEmpty()) {
                System.out.println("ERREUR: Le nom de l'item ne peut pas être vide.");
                return;
            }
            
            Item item = inventoryManager.findItemByName(itemName);
            if (item != null) {
                int quantity = inputHandler.readIntWithPrompt("Quantité à retirer: ");
                if (quantity > 0) {
                    boolean success = inventoryManager.removeStock(itemName, quantity);
                    if (success) {
                        System.out.println("Stock retiré!");
                    } else {
                        System.out.println("ERREUR: Pas assez de stock! Stock actuel: " + item.getStock());
                    }
                } else {
                    System.out.println("ERREUR: La quantité doit être positive.");
                }
            } else {
                System.out.println("ERREUR: Item non trouvé: " + itemName);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void addNewItem() {
        try {
            String itemName = inputHandler.readStringWithPrompt("Nom: ");
            if (itemName == null || itemName.trim().isEmpty()) {
                System.out.println("ERREUR: Le nom de l'item ne peut pas être vide.");
                return;
            }
            
            String priceInput = inputHandler.readStringWithPrompt("Prix: ");
            double itemPrice;
            try {
                itemPrice = Double.parseDouble(priceInput);
                if (itemPrice < 0) {
                    System.out.println("ERREUR: Le prix ne peut pas être négatif.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("ERREUR: Le prix doit être un nombre valide.");
                return;
            }
            
            int initialStock = inputHandler.readIntWithPrompt("Stock initial: ");
            if (initialStock < 0) {
                System.out.println("ERREUR: Le stock initial ne peut pas être négatif.");
                return;
            }
            
            String itemType = inputHandler.readStringWithPrompt("Type (main/snack/drink): ");
            if (!itemType.equals("main") && !itemType.equals("snack") && !itemType.equals("drink")) {
                System.out.println("ERREUR: Type invalide. Utilisez 'main', 'snack' ou 'drink'.");
                return;
            }
            
            Item newItem;
            if (itemType.equals("drink")) {
                String drinkSize = inputHandler.readStringWithPrompt("Taille: ");
                if (drinkSize == null || drinkSize.trim().isEmpty()) {
                    System.out.println("ERREUR: La taille ne peut pas être vide pour une boisson.");
                    return;
                }
                newItem = new Item(itemName, itemPrice, initialStock, itemType, drinkSize);
            } else {
                newItem = new Item(itemName, itemPrice, initialStock, itemType);
            }
            
            inventoryManager.addItem(newItem);
            System.out.println("Item ajouté avec succès!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
