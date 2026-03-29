package stackoverflow.service;

import stackoverflow.model.User;
import stackoverflow.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateReputation(String userId, int delta) {
        User user = findUserOrThrow(userId);
        user.updateReputation(delta);
        userRepository.save(user);
    }

    public User findUserOrThrow(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
