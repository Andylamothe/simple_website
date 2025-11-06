package refactoring.clean_code.objets;

// ===================
// ABSTRACT PRODUCTS
// ===================

interface EmailNotification {
    void send(String recipient, String subject, String message);
}

interface SMSNotification {
    void send(String phoneNumber, String message);
}

interface PushNotification {
    void send(String deviceToken, String title, String body);
}

interface SlackNotification {
    void send(String channel, String message);
}

// =========================
// GENERIC CONCRETE PRODUCT
// =========================

class GenericEmail implements EmailNotification {
    private final String priorityLabel;
    private final String config;
    
    public GenericEmail(String priorityLabel, String config) {
        this.priorityLabel = priorityLabel;
        this.config = config;
    }
    
    public void send(String recipient, String subject, String message) {
        System.out.println("=== " + priorityLabel + " EMAIL ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + (priorityLabel.equals("NORMAL") ? "" : "[" + priorityLabel + "] ") + subject);
        System.out.println("Message: " + message);
        System.out.println(config + "\n");
    }
}

class GenericSMS implements SMSNotification {
    private final String priorityLabel;
    private final String config;
    
    public GenericSMS(String priorityLabel, String config) {
        this.priorityLabel = priorityLabel;
        this.config = config;
    }
    
    public void send(String phoneNumber, String message) {
        String prefix = priorityLabel.equals("NORMAL") ? "" : "[" + priorityLabel + "] ";
        String truncated = (prefix + message).length() > 160 ? 
            (prefix + message).substring(0, 157) + "..." : prefix + message;
        
        System.out.println("=== " + priorityLabel + " SMS ===");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + truncated);
        System.out.println(config + "\n");
    }
}

class GenericPush implements PushNotification {
    private final String priorityLabel;
    private final String config;
    
    public GenericPush(String priorityLabel, String config) {
        this.priorityLabel = priorityLabel;
        this.config = config;
    }
    
    public void send(String deviceToken, String title, String body) {
        System.out.println("=== " + priorityLabel + " PUSH NOTIFICATION ===");
        System.out.println("Device: " + deviceToken);
        System.out.println("Title: " + (priorityLabel.equals("NORMAL") ? "" : "[" + priorityLabel + "] ") + title);
        System.out.println("Body: " + body);
        System.out.println(config + "\n");
    }
}

class GenericSlack implements SlackNotification {
    private final String priorityLabel;
    private final String emoji;
    private final String config;
    
    public GenericSlack(String priorityLabel, String emoji, String config) {
        this.priorityLabel = priorityLabel;
        this.emoji = emoji;
        this.config = config;
    }
    
    public void send(String channel, String message) {
        System.out.println("=== " + priorityLabel + " SLACK MESSAGE ===");
        System.out.println("Channel: " + channel);
        System.out.println("Message: " + emoji + " " + message);
        System.out.println(config + "\n");
    }
}

// ============================================
// ABSTRACT FACTORY
// ============================================

interface NotificationFactory {
    EmailNotification createEmail();
    SMSNotification createSMS();
    PushNotification createPush();
    SlackNotification createSlack();
}

// ============================================
// CONCRETE FACTORIES
// ============================================

class UrgentNotificationFactory implements NotificationFactory {
    public EmailNotification createEmail() {
        return new GenericEmail("URGENT", "Priority: HIGH\nUsing SMTP with encryption\nAdding red flag marker");
    }
    public SMSNotification createSMS() {
        return new GenericSMS("URGENT", "Using premium SMS gateway\nRetry on failure: 3 times");
    }
    public PushNotification createPush() {
        return new GenericPush("URGENT", "Sound: alarm.mp3\nBadge: 1\nVibration: enabled");
    }
    public SlackNotification createSlack() {
        return new GenericSlack("URGENT", ":rotating_light:", "Mention: @channel\nColor: danger");
    }
}

class NormalNotificationFactory implements NotificationFactory {
    public EmailNotification createEmail() {
        return new GenericEmail("NORMAL", "Priority: NORMAL\nUsing standard SMTP");
    }
    public SMSNotification createSMS() {
        return new GenericSMS("NORMAL", "Using standard SMS gateway");
    }
    public PushNotification createPush() {
        return new GenericPush("NORMAL", "Sound: default.mp3");
    }
    public SlackNotification createSlack() {
        return new GenericSlack("NORMAL", "", "Color: good");
    }
}


// ==============
// CLIENT CODE
// ==============

class NotificationService {
    private NotificationFactory factory;
    
    public NotificationService(NotificationFactory factory) {
        this.factory = factory;
    }
    
    public void setFactory(NotificationFactory factory) {
        this.factory = factory;
    }
    
    public void sendEmail(String recipient, String subject, String message) {
        factory.createEmail().send(recipient, subject, message);
    }
    
    public void sendSMS(String phoneNumber, String message) {
        factory.createSMS().send(phoneNumber, message);
    }
    
    public void sendPush(String deviceToken, String title, String body) {
        factory.createPush().send(deviceToken, title, body);
    }
    
    public void sendSlack(String channel, String message) {
        factory.createSlack().send(channel, message);
    }
}

// =======
// MAIN
// =======

public class NotificationDemo {
    public static void main(String[] args) {
        NotificationService service = new NotificationService(new UrgentNotificationFactory());
        
        System.out.println("Test 1: Urgent email");
        service.sendEmail("admin@company.com", "Server Down", "Production server is not responding");
        System.out.println("Test 2: Urgent push notification");
        service.sendPush("device_abc123", "Security Alert", "Unusual login activity detected");
        System.out.println("Test 3: Urgent Slack Alert");
        service.sendSlack("#incidents", "Database backup failed");

        service.setFactory(new NormalNotificationFactory());
        
        System.out.println("Test 4: Normal SMS");
        service.sendSMS("+1234567890", "Your order has been shipped and will arrive tomorrow");
        System.out.println("Test 5: Normal Slack");
        service.sendSlack("#general", "New feature deployed successfully");
    }
}