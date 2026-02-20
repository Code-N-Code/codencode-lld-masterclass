package notificationsystem.model;

import java.util.Set;

public class UserPreference {
    private final String userId;
    private final Set<ChannelType> preferredChannels;

    public UserPreference(String userId, Set<ChannelType> preferredChannels) {
        this.userId = userId;
        this.preferredChannels = preferredChannels;
    }

    public String getUserId() {
        return userId;
    }

    public Set<ChannelType> getPreferredChannels() {
        return preferredChannels;
    }
}
