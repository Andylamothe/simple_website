import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gère les entrées utilisateur de manière centralisée avec gestion robuste des erreurs
 */
public class InputHandler {
    private Scanner scanner;
    
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Lit un entier depuis l'entrée standard
     * @return l'entier lu
     * @throws IllegalArgumentException si l'entrée n'est pas un entier valide
     */
    public int readInt() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Consommer l'entrée invalide
            throw new IllegalArgumentException("Erreur: Entrée invalide. Un nombre entier est attendu.", e);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Erreur: Aucune entrée disponible.", e);
        }
    }
    
    /**
     * Lit un entier depuis l'entrée standard avec un prompt
     * @param prompt le message à afficher
     * @return l'entier lu
     * @throws IllegalArgumentException si l'entrée n'est pas un entier valide
     */
    public int readIntWithPrompt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }
    
    /**
     * Lit une chaîne de caractères depuis l'entrée standard
     * @return la chaîne lue
     * @throws IllegalArgumentException si aucune entrée n'est disponible
     */
    public String readString() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Erreur: Aucune entrée disponible.", e);
        }
    }
    
    /**
     * Lit une chaîne de caractères depuis l'entrée standard avec un prompt
     * @param prompt le message à afficher
     * @return la chaîne lue
     * @throws IllegalArgumentException si aucune entrée n'est disponible
     */
    public String readStringWithPrompt(String prompt) {
        System.out.print(prompt);
        return readString();
    }
    
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}

