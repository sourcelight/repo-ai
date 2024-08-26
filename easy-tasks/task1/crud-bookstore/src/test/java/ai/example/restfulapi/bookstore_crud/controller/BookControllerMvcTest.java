package ai.example.restfulapi.bookstore_crud.controller;

import ai.example.restfulapi.bookstore_crud.config.SecuredTest;
import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.entities.authentication.Role;
import ai.example.restfulapi.bookstore_crud.hateos.BookModelAssembler;
import ai.example.restfulapi.bookstore_crud.repositories.BookRepository;
import ai.example.restfulapi.bookstore_crud.repositories.authentication.UserRepository;
import ai.example.restfulapi.bookstore_crud.security.SecurityConfig;
import ai.example.restfulapi.bookstore_crud.services.BookService;
import ai.example.restfulapi.bookstore_crud.services.authentication.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ai.example.restfulapi.bookstore_crud.entities.authentication.User;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * this class performs http requests to test the controller methods
 *
 * @project: repo-ai
 */
@ExtendWith(MockitoExtension.class)
//@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//In this way it doesn't load the whole context, but only the classes specified in the annotation
//particualry doesn't load the new UserDetailsService class that would require the UserRepository
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = { SecurityConfig.class, BookController.class, BookService.class,
        BookModelAssembler.class, UserDetailsServiceImpl.class, UserRepository.class})
public class BookControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    private Book book;

    @BeforeEach
    public void setup() {

        book = Book.builder()
                .id(1L)
                .title("Spring Microservices in Action")
                .author("John Carnell")
                .genre("Technology")
                .price(45)
                .quantityAvailable(15)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByGenreContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByGenreContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
    }

    @Test
    //When you use @WithMockUser, Spring Security will automatically create a mock user with the username you specify.
    //end the UserDetailsService will load the user with the same username and it's deactivated
    //@WithMockUser(username = "john_doe", roles = { "USER" })
    public void testCreateBook() throws Exception {

        Book newBook = Book.builder()
                .id(1L)
                .title("Spring Microservices in Action")
                .author("John Carnell")
                .genre("Technology")
                .price(45)
                .quantityAvailable(15)
                .build();
        User user = new User("john_doe","john@example.com",passwordEncoder.encode("pwd"));
        Role role = new Role("ROLE_USER");
        Set set = Set.of(role);
        user.setRoles(set);
        when(userRepository.findByUsername(any())).thenReturn(user);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook))
                        //This is how you can authenticate the user and in this case the UserDetailsService is invoked  and will load the user with the same username
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("john_doe", "pwd"))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newBook.getTitle())))
                .andExpect(jsonPath("$.author", is(newBook.getAuthor())))
                .andExpect(jsonPath("$.genre", is(newBook.getGenre())))
                .andExpect(jsonPath("$.price", is(newBook.getPrice())))
                .andExpect(jsonPath("$.quantityAvailable", is(newBook.getQuantityAvailable())));
    }

    @Test
    @SecuredTest
    public void testGetBookById() throws Exception {
        mockMvc.perform(get("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.genre", is(book.getGenre())))
                .andExpect(jsonPath("$.price", is(book.getPrice())))
                .andExpect(jsonPath("$.quantityAvailable", is(book.getQuantityAvailable())));
    }

    @Test
    @SecuredTest
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(1)))
                .andExpect(jsonPath("$._embedded.bookList[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$._embedded.bookList[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$._embedded.bookList[0].genre", is(book.getGenre())))
                .andExpect(jsonPath("$._embedded.bookList[0].price", is(book.getPrice())))
                .andExpect(jsonPath("$._embedded.bookList[0].quantityAvailable", is(book.getQuantityAvailable())));
    }


    @Test
    public void testUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                //with the following annotation I force spring security to authenticate the user
                //and this time the UserDetailsService will load the user that is null(UserRepository.findByUsername returns null)
                //because is only mocked but not stubbed
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("john_doe", "pwd"))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }



    @Test
    @SecuredTest
    public void testUpdateBook() throws Exception {
        book.setTitle("Updated Title");

        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    @SecuredTest
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @SecuredTest
    public void testSearchBooks() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("title", "Spring")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(1)))
                .andExpect(jsonPath("$._embedded.bookList[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$._embedded.bookList[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$._embedded.bookList[0].genre", is(book.getGenre())))
                .andExpect(jsonPath("$._embedded.bookList[0].price", is(book.getPrice())))
                .andExpect(jsonPath("$._embedded.bookList[0].quantityAvailable", is(book.getQuantityAvailable())));
    }
}