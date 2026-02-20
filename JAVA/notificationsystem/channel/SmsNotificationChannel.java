package notificationsystem.channel;

import notificationsystem.model.Notification;

public class SmsNotificationChannel implements NotificationChannel {
    @Override
    public void send(Notification notification) {
        System.out.println(
                "Sending SMS to user " + notification.getUserId()
                        + ": " + notification.getMessage()
        );
    }
}
