/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.controller;

import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.hateos.BookModelAssembler;
import ai.example.restfulapi.bookstore_crud.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookModelAssembler assembler;

    // Create a new book
    @PostMapping
    public ResponseEntity<EntityModel<Book>> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(assembler.toModel(createdBook), HttpStatus.CREATED);
    }

    // Retrieve a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        return book.map(value -> new ResponseEntity<>(assembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Retrieve all books
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Book>>> getAllBooks() {
        List<EntityModel<Book>> books = bookService.getAllBooks().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(CollectionModel.of(books, linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel()), HttpStatus.OK);

    }

    // Update a book
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return new ResponseEntity<>(assembler.toModel(updatedBook), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search for books by title, author, or genre
    @GetMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<Book>>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {

        List<EntityModel<Book>> books = bookService.searchBooks(title, author, genre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(CollectionModel.of(books, linkTo(methodOn(BookController.class).searchBooks(title, author, genre)).withSelfRel()), HttpStatus.OK);
    }
}
