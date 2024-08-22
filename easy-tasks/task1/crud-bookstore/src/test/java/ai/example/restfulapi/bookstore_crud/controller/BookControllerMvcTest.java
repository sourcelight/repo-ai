package ai.example.restfulapi.bookstore_crud.controller;

import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.repositories.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

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
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Book book;

    @BeforeEach
    public void setup() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Spring Microservices in Action");
        book.setAuthor("John Carnell");
        book.setGenre("Technology");
        book.setPrice(45);
        book.setQuantityAvailable(15);

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
        when(bookRepository.findByGenreContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(book));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("Spring Microservices in Action");
        newBook.setAuthor("John Carnell");
        newBook.setGenre("Technology");
        newBook.setPrice(45.00);
        newBook.setQuantityAvailable(15);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newBook.getTitle())))
                .andExpect(jsonPath("$.author", is(newBook.getAuthor())))
                .andExpect(jsonPath("$.genre", is(newBook.getGenre())))
                .andExpect(jsonPath("$.price", is(newBook.getPrice())))
                .andExpect(jsonPath("$.quantityAvailable", is(newBook.getQuantityAvailable())));
    }

    @Test
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
    public void testUpdateBook() throws Exception {
        book.setTitle("Updated Title");

        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
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