/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.initializer;


import ai.example.restfulapi.bookstore_crud.entities.Book;

import ai.example.restfulapi.bookstore_crud.entities.authentication.Role;
import ai.example.restfulapi.bookstore_crud.entities.authentication.User;
import ai.example.restfulapi.bookstore_crud.repositories.BookRepository;
import ai.example.restfulapi.bookstore_crud.repositories.authentication.RoleRepository;
import ai.example.restfulapi.bookstore_crud.repositories.authentication.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BookRepository bookRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        Role roleUser = new Role("ROLE_USER");  // Using "ROLE_" prefix
        Role roleAdmin = new Role("ROLE_ADMIN"); // Using "ROLE_" prefix

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleUser);
        adminRoles.add(roleAdmin);

        User user = new User("john_doe", "john@example.com",passwordEncoder.encode("pwd"));
        user.setRoles(userRoles);

        User admin = new User("admin", "admin@example.com",passwordEncoder.encode("pwd"));
        admin.setRoles(adminRoles);

        userRepository.save(user);
        userRepository.save(admin);


        bookRepository.save(Book.builder()
                .title("The Lord of the Rings")
                .author("J.R.R. Tolkien")
                .price(39.99)
                .genre("World Science")
                .quantityAvailable(10)
                .build());
    }
}