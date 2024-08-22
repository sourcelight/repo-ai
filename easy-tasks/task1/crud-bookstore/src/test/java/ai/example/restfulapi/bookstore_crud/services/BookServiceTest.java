package ai.example.restfulapi.bookstore_crud.services;


import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        sampleBook = new Book();
        sampleBook.setTitle("Spring Boot in Action");
        sampleBook.setAuthor("Craig Walls");
        sampleBook.setGenre("Technology");
        sampleBook.setPrice(39.99);
        sampleBook.setQuantityAvailable(10);
    }

    @Test
    public void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book createdBook = bookService.createBook(sampleBook);
        assertNotNull(createdBook);
        assertEquals("Spring Boot in Action", createdBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testGetBookByIdFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(sampleBook));

        Optional<Book> retrievedBook = bookService.getBookById(1L);
        assertTrue(retrievedBook.isPresent());
        assertEquals("Spring Boot in Action", retrievedBook.get().getTitle());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.getAllBooks();
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateBookFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("Updated Title");
        updatedBookDetails.setAuthor("Updated Author");
        updatedBookDetails.setGenre("Updated Genre");
        updatedBookDetails.setPrice(49.99);
        updatedBookDetails.setQuantityAvailable(20);

        Book updatedBook = bookService.updateBook(1L, updatedBookDetails);
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Updated Author", updatedBook.getAuthor());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("Updated Title");
        updatedBookDetails.setAuthor("Updated Author");
        updatedBookDetails.setGenre("Updated Genre");
        updatedBookDetails.setPrice(49.99);
        updatedBookDetails.setQuantityAvailable(20);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBook(1L, updatedBookDetails);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteBookFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(sampleBook));
        doNothing().when(bookRepository).delete(any(Book.class));

        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).delete(any(Book.class));
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSearchBooksByTitle() {
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.searchBooks("Spring", null, null);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    public void testSearchBooksByAuthor() {
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.searchBooks(null, "Craig", null);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase(anyString());
    }

    @Test
    public void testSearchBooksByGenre() {
        when(bookRepository.findByGenreContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.searchBooks(null, null, "Technology");
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findByGenreContainingIgnoreCase(anyString());
    }
}