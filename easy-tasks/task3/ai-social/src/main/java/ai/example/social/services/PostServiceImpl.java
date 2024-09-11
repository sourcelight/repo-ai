/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.social.services;

import ai.example.social.entities.Like;
import ai.example.social.entities.Post;
import ai.example.social.entities.User;
import ai.example.social.repositories.LikeRepository;
import ai.example.social.repositories.PostRepository;
import ai.example.social.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void likePost(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            if (!likeRepository.existsByUserAndPost(user.get(), post.get())) {
                Like like = new Like();
                like.setUser(user.get());
                like.setPost(post.get());
                likeRepository.save(like);
            }
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            likeRepository.deleteByUserAndPost(user.get(), post.get());
        }
    }
}
