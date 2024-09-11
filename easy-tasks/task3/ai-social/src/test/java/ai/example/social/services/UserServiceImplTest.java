package ai.example.social.services;

import ai.example.social.entities.User;
import ai.example.social.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("sampleuser");
        user.setPassword("password123");
        user.setEmail("sampleuser@example.com");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("sampleuser");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        verify(userRepository, times(1)).findByUsername("sampleuser");
    }

    @Test
    public void testExistsByUsername() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        boolean exists = userService.existsByUsername("sampleuser");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("sampleuser");
    }

    @Test
    public void testFollowUser() {
        User followUser = new User();
        followUser.setId(2L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user)).thenReturn(Optional.of(followUser));

        userService.followUser(1L, 2L);

        verify(userRepository, times(2)).findById(anyLong());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(followUser);
        assertTrue(user.getFollowing().contains(followUser));
        assertTrue(followUser.getFollowers().contains(user));
    }

    @Test
    public void testUnfollowUser() {
        User followUser = new User();
        followUser.setId(2L);
        user.getFollowing().add(followUser);
        followUser.getFollowers().add(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user)).thenReturn(Optional.of(followUser));

        userService.unfollowUser(1L, 2L);

        verify(userRepository, times(2)).findById(anyLong());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(followUser);
        assertFalse(user.getFollowing().contains(followUser));
        assertFalse(followUser.getFollowers().contains(user));
    }
}