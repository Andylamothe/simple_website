// GOD CLASS - Violates Single Responsibility Principle
// This class does EVERYTHING related to payments
public class PaymentProcessor {
    
    public void processPayment(String paymentType, String gatewayType, 
                               double amount, String customerEmail) {
        
        // Validation logic mixed with business logic
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }
        
        if (customerEmail == null || !customerEmail.contains("@")) {
            System.out.println("Invalid email");
            return;
        }
        
        // Payment processing logic tightly coupled with gateway implementations
        if (paymentType.equals("CREDIT_CARD")) {
            if (gatewayType.equals("STRIPE")) {
                System.out.println("Connecting to Stripe API...");
                System.out.println("Encrypting credit card data for Stripe...");
                System.out.println("Processing $" + amount + " via Stripe");
                System.out.println("Stripe token: STR_" + System.currentTimeMillis());
                sendEmailViaGmail(customerEmail, "Payment Successful", 
                    "Your credit card payment of $" + amount + " was processed via Stripe");
            } else if (gatewayType.equals("PAYPAL")) {
                System.out.println("Connecting to PayPal API...");
                System.out.println("Encrypting credit card data for PayPal...");
                System.out.println("Processing $" + amount + " via PayPal");
                System.out.println("PayPal transaction ID: PP_" + System.currentTimeMillis());
                sendEmailViaGmail(customerEmail, "Payment Successful", 
                    "Your credit card payment of $" + amount + " was processed via PayPal");
            } else if (gatewayType.equals("SQUARE")) {
                System.out.println("Connecting to Square API...");
                System.out.println("Encrypting credit card data for Square...");
                System.out.println("Processing $" + amount + " via Square");
                System.out.println("Square payment ID: SQ_" + System.currentTimeMillis());
                sendEmailViaSendGrid(customerEmail, "Payment Successful", 
                    "Your credit card payment of $" + amount + " was processed via Square");
            }
        } else if (paymentType.equals("PAYPAL_WALLET")) {
            if (gatewayType.equals("STRIPE")) {
                System.out.println("Stripe doesn't support PayPal wallet directly");
                System.out.println("Redirecting to PayPal for authentication...");
                System.out.println("Processing $" + amount + " via PayPal wallet through Stripe");
                sendEmailViaGmail(customerEmail, "Payment Successful", 
                    "Your PayPal wallet payment of $" + amount + " was processed");
            } else if (gatewayType.equals("PAYPAL")) {
                System.out.println("Authenticating PayPal wallet...");
                System.out.println("Processing $" + amount + " via PayPal wallet");
                System.out.println("PayPal wallet transaction: PW_" + System.currentTimeMillis());
                sendEmailViaGmail(customerEmail, "Payment Successful", 
                    "Your PayPal wallet payment of $" + amount + " was processed");
            }
        } else if (paymentType.equals("BANK_TRANSFER")) {
            if (gatewayType.equals("STRIPE")) {
                System.out.println("Initiating ACH transfer via Stripe...");
                System.out.println("Verifying bank account...");
                System.out.println("Processing $" + amount + " via bank transfer");
                System.out.println("Transfer will complete in 3-5 business days");
                sendEmailViaGmail(customerEmail, "Bank Transfer Initiated", 
                    "Your bank transfer of $" + amount + " has been initiated via Stripe");
            } else if (gatewayType.equals("PAYPAL")) {
                System.out.println("PayPal doesn't support direct bank transfers");
            } else if (gatewayType.equals("SQUARE")) {
                System.out.println("Square doesn't support bank transfers");
            }
        } else if (paymentType.equals("CRYPTO")) {
            if (gatewayType.equals("STRIPE")) {
                System.out.println("Stripe doesn't support cryptocurrency");
            } else if (gatewayType.equals("PAYPAL")) {
                System.out.println("Connecting to PayPal crypto service...");
                System.out.println("Processing $" + amount + " in cryptocurrency");
                sendEmailViaGmail(customerEmail, "Crypto Payment Successful", 
                    "Your cryptocurrency payment of $" + amount + " was processed");
            }
        }
        
        // Logging logic mixed in
        logTransaction(paymentType, gatewayType, amount, customerEmail);
    }
    
    // Email sending logic coupled with payment processing
    private void sendEmailViaGmail(String to, String subject, String body) {
        System.out.println("Sending email via Gmail SMTP...");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
    
    private void sendEmailViaSendGrid(String to, String subject, String body) {
        System.out.println("Sending email via SendGrid API...");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
    
    // Database/logging logic also in the same class
    private void logTransaction(String paymentType, String gatewayType, 
                                double amount, String email) {
        System.out.println("=== Logging Transaction ===");
        System.out.println("Type: " + paymentType);
        System.out.println("Gateway: " + gatewayType);
        System.out.println("Amount: $" + amount);
        System.out.println("Customer: " + email);
        System.out.println("Timestamp: " + System.currentTimeMillis());
    }
    
    // Refund logic also crammed in here
    public void processRefund(String paymentType, String gatewayType, 
                             double amount, String transactionId) {
        System.out.println("Processing refund of $" + amount);
        
        if (gatewayType.equals("STRIPE")) {
            System.out.println("Connecting to Stripe refund API...");
            System.out.println("Refunding transaction: " + transactionId);
        } else if (gatewayType.equals("PAYPAL")) {
            System.out.println("Connecting to PayPal refund API...");
            System.out.println("Refunding transaction: " + transactionId);
        } else if (gatewayType.equals("SQUARE")) {
            System.out.println("Connecting to Square refund API...");
            System.out.println("Refunding transaction: " + transactionId);
        }
    }
    
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        
        System.out.println("=== Test 1: Credit Card via Stripe ===");
        processor.processPayment("CREDIT_CARD", "STRIPE", 99.99, "customer@example.com");
        
        System.out.println("\n=== Test 2: PayPal Wallet via PayPal ===");
        processor.processPayment("PAYPAL_WALLET", "PAYPAL", 149.99, "user@test.com");
        
        System.out.println("\n=== Test 3: Bank Transfer via Stripe ===");
        processor.processPayment("BANK_TRANSFER", "STRIPE", 500.00, "business@company.com");
        
        System.out.println("\n=== Test 4: Crypto via PayPal ===");
        processor.processPayment("CRYPTO", "PAYPAL", 250.00, "crypto@fan.com");
        
        System.out.println("\n=== Test 5: Refund ===");
        processor.processRefund("CREDIT_CARD", "STRIPE", 99.99, "STR_123456");
    }
}