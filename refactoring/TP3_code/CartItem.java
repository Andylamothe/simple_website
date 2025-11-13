/**
 * Représente un item dans le panier (peut être un item simple ou un trio)
 */
public class CartItem {
    private Item item;
    private boolean isTrio;
    private Item trioSnack;  // Si c'est un trio
    private Item trioDrink;  // Si c'est un trio
    
    // Pour un item simple
    public CartItem(Item item) {
        this.item = item;
        this.isTrio = false;
        this.trioSnack = null;
        this.trioDrink = null;
    }
    
    // Pour un trio
    public CartItem(Item main, Item snack, Item drink) {
        this.item = main;
        this.trioSnack = snack;
        this.trioDrink = drink;
        this.isTrio = true;
    }
    
    public Item getItem() {
        return item;
    }
    
    public boolean isTrio() {
        return isTrio;
    }
    
    public Item getTrioSnack() {
        return trioSnack;
    }
    
    public Item getTrioDrink() {
        return trioDrink;
    }
    
    // Calculer le prix
    public double getPrice() {
        if (isTrio) {
            double total = item.getPrice() + trioSnack.getPrice() + trioDrink.getPrice();
            return Math.round(total * 0.85 * 100.0) / 100.0; // 15% rabais, arrondi à 2 décimales
        } else {
            return Math.round(item.getPrice() * 100.0) / 100.0;
        }
    }
    
    public String getDescription() {
        if (isTrio) {
            return "TRIO: " + item.getName() + " + " + trioSnack.getName() + 
                   " + " + trioDrink.getName() + " (15% rabais)";
        } else {
            return item.getName();
        }
    }
}


