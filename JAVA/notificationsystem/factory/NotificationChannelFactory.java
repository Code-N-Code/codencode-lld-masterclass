package notificationsystem.factory;

import notificationsystem.channel.EmailNotificationChannel;
import notificationsystem.channel.NotificationChannel;
import notificationsystem.channel.PushNotificationChannel;
import notificationsystem.channel.SmsNotificationChannel;
import notificationsystem.model.ChannelType;

public class NotificationChannelFactory {
    public static NotificationChannel getChannel(ChannelType channelType) {
        return switch (channelType) {
            case EMAIL -> new EmailNotificationChannel();
            case SMS -> new SmsNotificationChannel();
            case PUSH -> new PushNotificationChannel();
        };
    }
}
