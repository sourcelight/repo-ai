package ai.example.social.controllers;

import ai.example.social.entities.User;
import ai.example.social.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("sampleuser");
        user.setPassword("password123");
        user.setEmail("sampleuser@example.com");
    }

    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("sampleuser"))
                .andExpect(jsonPath("$.email").value("sampleuser@example.com"));
    }

    @Test
    public void testCreateUserConflict() throws Exception {
        Mockito.when(userService.existsByUsername(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        Mockito.when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{username}", "sampleuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("sampleuser"))
                .andExpect(jsonPath("$.email").value("sampleuser@example.com"));
    }

    @Test
    public void testGetUserByUsernameNotFound() throws Exception {
        Mockito.when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{username}", "sampleuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFollowUser() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/follow/{friendUserId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).followUser(anyLong(), anyLong());
    }

    @Test
    public void testUnfollowUser() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/unfollow/{friendUserId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).unfollowUser(anyLong(), anyLong());
    }
}