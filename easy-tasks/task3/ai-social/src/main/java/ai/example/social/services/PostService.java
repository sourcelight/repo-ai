package ai.example.social.services;

import ai.example.social.entities.Post;

import java.util.List;

public interface PostService {

    /**
     * Creates a new post.
     *
     * @param post The post to be created.
     * @return The created post.
     */
    Post createPost(Post post);

    /**
     * Retrieves all the posts.
     *
     * @return A list of all posts.
     */
    List<Post> getAllPosts();

    /**
     * Likes a post by a user.
     *
     * @param postId The ID of the post to be liked.
     * @param userId The ID of the user who likes the post.
     */
    void likePost(Long postId, Long userId);

    /**
     * Unlikes a post by a user.
     *
     * @param postId The ID of the post to be unliked.
     * @param userId The ID of the user who unlikes the post.
     */
    void unlikePost(Long postId, Long userId);
}
