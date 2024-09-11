package ai.example.social.services;

import ai.example.social.entities.Like;
import ai.example.social.entities.Post;
import ai.example.social.entities.User;
import ai.example.social.repositories.LikeRepository;
import ai.example.social.repositories.PostRepository;
import ai.example.social.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Post();
        post.setId(1L);
        post.setTitle("Sample Title");
        post.setBody("Sample Body");

        user = new User();
        user.setId(1L);
        user.setUsername("sampleuser");
        user.setPassword("password123");
        user.setEmail("sampleuser@example.com");
    }

    @Test
    public void testCreatePost() {
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(post);

        assertEquals(post.getId(), createdPost.getId());
        assertEquals(post.getTitle(), createdPost.getTitle());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testGetAllPosts() {
        List<Post> posts = Arrays.asList(post);
        when(postRepository.findAll()).thenReturn(posts);

        List<Post> retrievedPosts = postService.getAllPosts();

        assertEquals(1, retrievedPosts.size());
        assertEquals(post.getId(), retrievedPosts.get(0).getId());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testLikePost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(likeRepository.existsByUserAndPost(any(User.class), any(Post.class))).thenReturn(false);

        postService.likePost(1L, 1L);

        verify(likeRepository, times(1)).existsByUserAndPost(user, post);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    public void testUnlikePost() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        postService.unlikePost(1L, 1L);

        verify(likeRepository, times(1)).deleteByUserAndPost(user, post);
    }
}