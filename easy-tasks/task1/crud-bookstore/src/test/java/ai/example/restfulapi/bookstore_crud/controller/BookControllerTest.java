package ai.example.restfulapi.bookstore_crud.controller;

import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.hateos.BookModelAssembler;
import ai.example.restfulapi.bookstore_crud.services.BookService;
import ai.example.restfulapi.bookstore_crud.utilities.HateoasUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * this class doesn't perform http requests, it tests the controller methods directly
 *
 * @project: repo-ai
 */
@ExtendWith(MockitoExtension.class)
//@SpringBootTest(properties = { "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration" })
//I've used the application-test.yml file instead of the above annotation
@ActiveProfiles("test")
@SpringBootTest
public class BookControllerTest {


    @Autowired
    BookController bookController;

    @MockBean
    BookService bookService;

    @Autowired
    private BookModelAssembler assembler;

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

        //when(bookService.createBook(any(Book.class))).thenReturn(book);
        //when(assembler.toModel(any(Book.class))).thenReturn(EntityModel.of(book));
    }

    @Test
    public void createBookReturnsCreatedBook() {

        when(bookService.createBook(any(Book.class))).thenReturn(book);
        ResponseEntity<EntityModel<Book>> response = bookController.createBook(book);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody().getContent());
    }

    @Test
    public void getBookByIdReturnsBookWhenFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<EntityModel<Book>> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody().getContent());
    }

    @Test
    public void getBookByIdReturnsNotFoundWhenNotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Book>> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getAllBooksReturnsAllBooks() {
        List<Book> books = Arrays.asList(book, book);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<CollectionModel<EntityModel<Book>>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, HateoasUtils.extractEntities(response));
    }

    @Test
    public void updateBookReturnsUpdatedBookWhenFound() {

        when(bookService.updateBook(1L, book)).thenReturn(book);

        ResponseEntity<EntityModel<Book>> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody().getContent());
    }

    @Test
    public void updateBookReturnsNotFoundWhenNotFound() {
        when(bookService.updateBook(1L, book)).thenThrow(RuntimeException.class);

        ResponseEntity<EntityModel<Book>> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteBookReturnsNoContent() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void searchBooksReturnsMatchingBooks() {
        List<Book> books = Arrays.asList(book, book);
        when(bookService.searchBooks("title", "author", "genre")).thenReturn(books);

        ResponseEntity<CollectionModel<EntityModel<Book>>> response = bookController.searchBooks("title", "author", "genre");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, HateoasUtils.extractEntities(response));
    }
}