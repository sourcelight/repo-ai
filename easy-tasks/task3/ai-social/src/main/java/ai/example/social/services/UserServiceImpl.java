/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.social.services;

import ai.example.social.entities.User;
import ai.example.social.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void followUser(Long userId, Long followUserId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> followUser = userRepository.findById(followUserId);

        if (user.isPresent() && followUser.isPresent()) {
            user.get().getFollowing().add(followUser.get());
            followUser.get().getFollowers().add(user.get());
            userRepository.save(user.get());
            userRepository.save(followUser.get());
        }
    }

    @Override
    public void unfollowUser(Long userId, Long followUserId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> followUser = userRepository.findById(followUserId);

        if (user.isPresent() && followUser.isPresent()) {
            user.get().getFollowing().remove(followUser.get());
            followUser.get().getFollowers().remove(user.get());
            userRepository.save(user.get());
            userRepository.save(followUser.get());
        }
    }



}

