
class EmailUrgentNotification {
    private String recipient;
    private String subject;
    private String message;
    
    public EmailUrgentNotification(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== URGENT EMAIL ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: [URGENT] " + subject);
        System.out.println("Message: " + message);
        System.out.println("Priority: HIGH");
        System.out.println("Using SMTP with encryption");
        System.out.println("Adding red flag marker");
    }
}

class EmailNormalNotification {
    private String recipient;
    private String subject;
    private String message;
    
    public EmailNormalNotification(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== NORMAL EMAIL ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Priority: NORMAL");
        System.out.println("Using standard SMTP");
    }
}

class SMSUrgentNotification {
    private String phoneNumber;
    private String message;
    
    public SMSUrgentNotification(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== URGENT SMS ===");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: [URGENT] " + truncateForSMS(message));
        System.out.println("Using premium SMS gateway");
        System.out.println("Retry on failure: 3 times");
    }
    
    private String truncateForSMS(String msg) {
        return msg.length() > 160 ? msg.substring(0, 157) + "..." : msg;
    }
}

class SMSNormalNotification {
    private String phoneNumber;
    private String message;
    
    public SMSNormalNotification(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== NORMAL SMS ===");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + truncateForSMS(message));
        System.out.println("Using standard SMS gateway");
    }
    
    private String truncateForSMS(String msg) {
        return msg.length() > 160 ? msg.substring(0, 157) + "..." : msg;
    }
}

class PushUrgentNotification {
    private String deviceToken;
    private String title;
    private String body;
    
    public PushUrgentNotification(String deviceToken, String title, String body) {
        this.deviceToken = deviceToken;
        this.title = title;
        this.body = body;
    }
    
    public void send() {
        System.out.println("=== URGENT PUSH NOTIFICATION ===");
        System.out.println("Device: " + deviceToken);
        System.out.println("Title: [URGENT] " + title);
        System.out.println("Body: " + body);
        System.out.println("Sound: alarm.mp3");
        System.out.println("Badge: 1");
        System.out.println("Vibration: enabled");
    }
}

class PushNormalNotification {
    private String deviceToken;
    private String title;
    private String body;
    
    public PushNormalNotification(String deviceToken, String title, String body) {
        this.deviceToken = deviceToken;
        this.title = title;
        this.body = body;
    }
    
    public void send() {
        System.out.println("=== NORMAL PUSH NOTIFICATION ===");
        System.out.println("Device: " + deviceToken);
        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
        System.out.println("Sound: default.mp3");
    }
}

class SlackUrgentNotification {
    private String channel;
    private String message;
    
    public SlackUrgentNotification(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== URGENT SLACK MESSAGE ===");
        System.out.println("Channel: " + channel);
        System.out.println("Message: :rotating_light: [URGENT] " + message);
        System.out.println("Mention: @channel");
        System.out.println("Color: danger");
    }
}

class SlackNormalNotification {
    private String channel;
    private String message;
    
    public SlackNormalNotification(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }
    
    public void send() {
        System.out.println("=== NORMAL SLACK MESSAGE ===");
        System.out.println("Channel: " + channel);
        System.out.println("Message: " + message);
        System.out.println("Color: good");
    }
}

// Client code that has to know about all combinations
public class NotificationDemo {
    public static void main(String[] args) {
        // Code duplication and tight coupling everywhere
        // Adding a new priority level or channel means creating many new classes
        
        System.out.println("Test 1: Urgent email");
        EmailUrgentNotification email1 = new EmailUrgentNotification(
            "admin@company.com", 
            "Server Down", 
            "Production server is not responding"
        );
        email1.send();

        System.out.println("\nTest 2: Urgent push notification");
        PushUrgentNotification push1 = new PushUrgentNotification(
            "device_token_abc123",
            "Security Alert",
            "Unusual login activity detected"
        );
        push1.send();

        System.out.println("\nTest 3: Urgent Slack alert");
        SlackUrgentNotification slack2 = new SlackUrgentNotification(
            "#incidents",
            "Database backup failed"
        );
        slack2.send();
        
        System.out.println("\nTest 4: Normal SMS");
        SMSNormalNotification sms1 = new SMSNormalNotification(
            "+1234567890",
            "Your order has been shipped and will arrive tomorrow"
        );
        sms1.send();
        
        System.out.println("\nTest 5: Normal Slack message");
        SlackNormalNotification slack1 = new SlackNormalNotification(
            "#general",
            "New feature deployed successfully"
        );
        slack1.send();
    }
}