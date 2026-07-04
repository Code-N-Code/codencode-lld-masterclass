package splitwise.services;

import splitwise.models.Group;
import splitwise.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupService {
    private final Map<String, Group> groupMap;

    public GroupService() {
        this.groupMap = new ConcurrentHashMap<>();
    }

    public void createGroup(String groupId, String name, String desc) {
        groupMap.put(groupId, new Group(groupId, name, desc));
    }

    public Group getGroup(String groupId) {
        return groupMap.get(groupId);
    }

    public boolean contains(String groupId) {
        return groupMap.containsKey(groupId);
    }

    // Notice how we pass the actual User object here, keeping GroupService
    // decoupled from needing to know how to look up users by ID.
    public void addUserToGroup(String groupId, User user) {
        Group group = getGroup(groupId);
        if (group != null && user != null) {
            group.addMember(user);
        }
    }
}
