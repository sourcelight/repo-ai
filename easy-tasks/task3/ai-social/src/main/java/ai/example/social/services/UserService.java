package ai.example.social.services;

import ai.example.social.entities.User;

import java.util.Optional;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user The user to be created.
     * @return The created user.
     */
    User createUser(User user);

    /**
     * Finds a user by username.
     *
     * @param username The username of the user to be found.
     * @return An Optional containing the found user or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return True if a user with the username exists, otherwise false.
     */
    boolean existsByUsername(String username);

    /**
     * Follows a user.
     *
     * @param userId The ID of the user who is following.
     * @param followUserId The ID of the user to be followed.
     */
    void followUser(Long userId, Long followUserId);

    /**
     * Unfollows a user.
     *
     * @param userId The ID of the user who is unfollowing.
     * @param followUserId The ID of the user to be unfollowed.
     */
    void unfollowUser(Long userId, Long followUserId);
}
