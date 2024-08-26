/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.repositories.authentication;

import ai.example.restfulapi.bookstore_crud.entities.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}