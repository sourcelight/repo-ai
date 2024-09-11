package ai.example.social.controllers;

import ai.example.social.entities.Post;
import ai.example.social.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private Post post;

    @BeforeEach
    public void setUp() {
        post = new Post();
        post.setId(1L);
        post.setTitle("Sample Title");
        post.setBody("Sample Body");
    }

    @Test
    public void testCreatePost() throws Exception {
        Mockito.when(postService.createPost(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Title"))
                .andExpect(jsonPath("$.body").value("Sample Body"));
    }

    @Test
    public void testGetAllPosts() throws Exception {
        List<Post> posts = Arrays.asList(post);
        Mockito.when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Sample Title"))
                .andExpect(jsonPath("$[0].body").value("Sample Body"));
    }

    @Test
    public void testLikePost() throws Exception {
        mockMvc.perform(post("/api/posts/{postId}/like", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk());

        Mockito.verify(postService, Mockito.times(1)).likePost(anyLong(), anyLong());
    }

    @Test
    public void testUnlikePost() throws Exception {
        mockMvc.perform(post("/api/posts/{postId}/unlike", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk());

        Mockito.verify(postService, Mockito.times(1)).unlikePost(anyLong(), anyLong());
    }
}