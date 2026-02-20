package notificationsystem.api;

import notificationsystem.model.Notification;
import notificationsystem.service.NotificationDispatcher;

public class NotificationService {
    private final NotificationDispatcher dispatcher;

    public NotificationService(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void sendNotification(Notification notification) {
        dispatcher.dispatch(notification);
    }
}
