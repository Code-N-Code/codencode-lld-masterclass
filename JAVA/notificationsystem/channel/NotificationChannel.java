package notificationsystem.channel;


import notificationsystem.model.Notification;

public interface NotificationChannel {
    void send(Notification notification);
}
