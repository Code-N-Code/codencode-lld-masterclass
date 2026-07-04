package splitwise.services;

import splitwise.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final Map<String, User> userMap;

    public UserService() {
        this.userMap = new ConcurrentHashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public User getUser(String userId) {
        return userMap.get(userId);
    }

    public boolean userExists(String userId) {
        return userMap.containsKey(userId);
    }
}
