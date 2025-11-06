package refactoring.clean_code.objets;

import java.util.*;

// ============================================
// IMPLEMENTATION: Payment Gateways
// ============================================

interface PaymentGateway {
    String process(double amount, String data);
    boolean supports(String type);
    default String refund(String txId, double amount) {
        System.out.println("Refunding " + txId + ": $" + amount);
        return "REFUND_" + System.currentTimeMillis();
    }
}

class StripeGateway implements PaymentGateway {
    public String process(double amount, String data) {
        System.out.println("Stripe processing $" + amount);
        return "STR_" + System.currentTimeMillis();
    }
    public boolean supports(String type) {
        return Set.of("CREDIT_CARD", "BANK_TRANSFER").contains(type);
    }
}

class PayPalGateway implements PaymentGateway {
    public String process(double amount, String data) {
        System.out.println("PayPal processing $" + amount);
        return "PP_" + System.currentTimeMillis();
    }
    public boolean supports(String type) {
        return Set.of("CREDIT_CARD", "PAYPAL_WALLET", "CRYPTO").contains(type);
    }
}



// ============================================
// ABSTRACTION: Payment Methods
// ============================================

abstract class PaymentMethod {
    protected PaymentGateway gateway;
    protected String type;
    
    public PaymentMethod(PaymentGateway gateway, String type) {
        this.gateway = gateway;
        this.type = type;
    }
    
    public String execute(double amount, Map<String, String> data) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        if (!gateway.supports(type)) throw new UnsupportedOperationException("Gateway doesn't support " + type);
        
        System.out.println("Processing " + type + " payment...");
        return gateway.process(amount, prepareData(data));
    }
    
    protected abstract String prepareData(Map<String, String> data);
    public String getType() { return type; }
}

class CreditCardPayment extends PaymentMethod {
    public CreditCardPayment(PaymentGateway gateway) {
        super(gateway, "CREDIT_CARD");
    }
    protected String prepareData(Map<String, String> data) {
        return data.get("cardNumber") + "|" + data.get("cvv");
    }
}

class PayPalWalletPayment extends PaymentMethod {
    public PayPalWalletPayment(PaymentGateway gateway) {
        super(gateway, "PAYPAL_WALLET");
    }
    protected String prepareData(Map<String, String> data) {
        System.out.println("Authenticating PayPal wallet...");
        return data.get("email");
    }
}

class BankTransferPayment extends PaymentMethod {
    public BankTransferPayment(PaymentGateway gateway) {
        super(gateway, "BANK_TRANSFER");
    }
    protected String prepareData(Map<String, String> data) {
        System.out.println("Verifying bank account... (3-5 business days)");
        return data.get("accountNumber") + "|" + data.get("routingNumber");
    }
}

class CryptoPayment extends PaymentMethod {
    public CryptoPayment(PaymentGateway gateway) {
        super(gateway, "CRYPTO");
    }
    protected String prepareData(Map<String, String> data) {
        return data.get("walletAddress");
    }
}

// ============================================
// PROCESSOR & SERVICES
// ============================================

class PaymentProcessor {
    private final Runnable notifier;
    private final Runnable logger;
    
    public PaymentProcessor(Runnable notifier, Runnable logger) {
        this.notifier = notifier;
        this.logger = logger;
    }
    
    public String process(PaymentMethod method, double amount, Map<String, String> data) {
        String txId = method.execute(amount, data);
        logger.run();
        notifier.run();
        return txId;
    }
    
    public String refund(PaymentGateway gateway, String txId, double amount) {
        return gateway.refund(txId, amount);
    }
}

// ============================================
// MAIN
// ============================================

public class PaymentSystemDemo {
    public static void main(String[] args) {
        PaymentGateway stripe = new StripeGateway();
        PaymentGateway paypal = new PayPalGateway();
        
        PaymentProcessor processor = new PaymentProcessor(
            () -> System.out.println("✉ Email sent"),
            () -> System.out.println("📝 Transaction logged\n")
        );
        
        // Test 1: Credit Card via Stripe
        System.out.println("=== Test 1: Credit Card via Stripe ===");
        processor.process(
            new CreditCardPayment(stripe), 
            99.99, 
            Map.of("cardNumber", "4111111111111111", "cvv", "123", "email", "customer@example.com")
        );
        

        // Test 2: PayPal Wallet
        System.out.println("=== Test 2: PayPal Wallet via PayPal===");
        processor.process(
            new PayPalWalletPayment(paypal), 
            75.50, 
            Map.of("email", "wallet@user.com")
        );
        
        // Test 3: Bank Transfer via Stripe
        System.out.println("=== Test 3: Bank Transfer via Stripe ===");
        processor.process(
            new BankTransferPayment(stripe), 
            500.00, 
            Map.of("accountNumber", "123456789", "routingNumber", "987654321", "email", "business@company.com")
        );
        
        // Test 4: Crypto via PayPal
        System.out.println("=== Test 4: Crypto via PayPal ===");
        processor.process(
            new CryptoPayment(paypal), 
            250.00, 
            Map.of("walletAddress", "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb", "email", "crypto@fan.com")
        );
        
        // Test 5: Refund
        System.out.println("=== Test 5: Refund ===");
        processor.refund(stripe, "STR_123456", 99.99);
    }
}