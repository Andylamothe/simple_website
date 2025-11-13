import java.util.Scanner;

/**
 * Gère les entrées utilisateur de manière centralisée
 */
public class InputHandler {
    private Scanner scanner;
    
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }
    
    public int readInt() {
        int value = scanner.nextInt();
        scanner.nextLine(); 
        return value;
    }
    
    public int readIntWithPrompt(String prompt) {
        System.out.print(prompt);
        int value = scanner.nextInt();
        scanner.nextLine(); 
        return value;
    }
    
    public String readString() {
        scanner.nextLine(); 
        return scanner.nextLine();
    }
    
    public String readStringWithPrompt(String prompt) {
        System.out.print(prompt);

        return scanner.nextLine();
    }
    
    public double readDouble() {
        return scanner.nextDouble();
    }
    
    public double readDoubleWithPrompt(String prompt) {
        System.out.print(prompt);
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
    
    public void close() {
        scanner.close();
    }
}

