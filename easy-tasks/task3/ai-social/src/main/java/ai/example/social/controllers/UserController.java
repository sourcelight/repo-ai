/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.social.controllers;

import ai.example.social.entities.User;
import ai.example.social.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @PostMapping("/{userId}/follow/{friendUserId}")
    public ResponseEntity<Void> followUser(@PathVariable Long userId, @PathVariable Long friendUserId) {
        userService.followUser(userId, friendUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{userId}/unfollow/{friendUserId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId, @PathVariable Long friendUserId) {
        userService.unfollowUser(userId, friendUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}