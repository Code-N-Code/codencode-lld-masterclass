package notificationsystem.channel;

import notificationsystem.model.Notification;

public class PushNotificationChannel implements NotificationChannel {
    @Override
    public void send(Notification notification) {
        System.out.println(
                "Sending PUSH to user " + notification.getUserId()
                        + ": " + notification.getMessage()
        );
    }
}
