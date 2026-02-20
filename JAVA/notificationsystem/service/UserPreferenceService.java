package notificationsystem.service;

import notificationsystem.model.ChannelType;
import notificationsystem.model.UserPreference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserPreferenceService {
    private final Map<String, UserPreference> preferences = new ConcurrentHashMap<>();

    public void savePreference(UserPreference preference) {
        preferences.put(preference.getUserId(), preference);
    }

    public UserPreference getPreference(String userId) {
        return preferences.getOrDefault(
                userId,
                new UserPreference(userId, Set.of(ChannelType.EMAIL))
        );
    }
}
