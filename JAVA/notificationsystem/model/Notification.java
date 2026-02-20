package notificationsystem.model;

public class Notification {
    private final String userId;
    private final String message;

    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

}
