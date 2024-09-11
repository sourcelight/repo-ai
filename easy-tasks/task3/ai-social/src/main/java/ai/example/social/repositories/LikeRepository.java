package ai.example.social.repositories;

import ai.example.social.entities.Like;
import ai.example.social.entities.Post;
import ai.example.social.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
}
