package notificationsystem;

import notificationsystem.api.AsyncNotificationService;
import notificationsystem.api.NotificationService;
import notificationsystem.model.ChannelType;
import notificationsystem.model.Notification;
import notificationsystem.model.UserPreference;
import notificationsystem.service.NotificationDispatcher;
import notificationsystem.service.UserPreferenceService;

import java.util.Set;

public class Main {
    public static void main(String[] args) {

        // Defining preference service.
        UserPreferenceService preferenceService = new UserPreferenceService();

        // Defining user preference with Email and SMS as preferred channels.
        preferenceService.savePreference(
                new UserPreference(
                        "user123",
                        Set.of(ChannelType.EMAIL, ChannelType.SMS)
                )
        );

        // Defining notification dispatcher
        NotificationDispatcher dispatcher =
                new NotificationDispatcher(preferenceService);

        // Defining async service.
        AsyncNotificationService notificationService =
                new AsyncNotificationService(dispatcher);

        // Defining synchronous service.
        NotificationService service = new NotificationService(dispatcher);

        // Defining notification to send through multiple channels.
        Notification notification =
                new Notification(
                        "user123",
                        "Your order has been shipped!"
                );

        // Sending notification through synchronous service.
        service.sendNotification(notification);

        // Sending notification through asynchronous service.
        notificationService.sendNotification(notification);

    }
}
