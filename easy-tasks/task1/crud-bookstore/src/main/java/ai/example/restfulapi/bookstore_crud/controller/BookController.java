/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.controller;

import ai.example.restfulapi.bookstore_crud.entities.Book;
import ai.example.restfulapi.bookstore_crud.hateos.BookModelAssembler;
import ai.example.restfulapi.bookstore_crud.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Create a new book",
            responses = {
                    @ApiResponse(description = "Book created successfully", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Invalid input", responseCode = "400", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<EntityModel<Book>> createBook(
            @Parameter(description = "Details of the book to be created", required = true)
            @Valid @RequestBody Book book)
    {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(assembler.toModel(createdBook), HttpStatus.CREATED);
    }

    // Retrieve a book by ID
    @Operation(
            summary = "Retrieve a book by ID",
            responses = {
                    @ApiResponse(description = "Book found", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Book not found", responseCode = "404", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        return book.map(value -> new ResponseEntity<>(assembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Retrieve all books
    @Operation(
            summary = "Retrieve all books",
            responses = {
                    @ApiResponse(description = "Books retrieved successfully", responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Book>>> getAllBooks() {
        List<EntityModel<Book>> books = bookService.getAllBooks().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(CollectionModel.of(books, linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel()), HttpStatus.OK);

    }

    // Update a book
    @Operation(
            summary = "Update a book by ID",
            responses = {
                    @ApiResponse(description = "Book updated successfully", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Book not found", responseCode = "404", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Book>> updateBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated details of the book", required = true)
            @Valid @RequestBody Book bookDetails) {
        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return new ResponseEntity<>(assembler.toModel(updatedBook), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a book
    @Operation(
            summary = "Delete a book by ID",
            responses = {
                    @ApiResponse(description = "Book deleted successfully", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Book not found", responseCode = "404", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search for books by title, author, or genre
    @Operation(
            summary = "Search books by title, author, or genre",
            responses = {
                    @ApiResponse(description = "Books retrieved successfully", responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<Book>>> searchBooks(
            @Parameter(description = "Title of the book(s) to search for")
            @RequestParam(required = false) String title,
            @Parameter(description = "Author of the book(s) to search for")
            @RequestParam(required = false) String author,
            @Parameter(description = "Genre of the book(s) to search for")
            @RequestParam(required = false) String genre) {

        List<EntityModel<Book>> books = bookService.searchBooks(title, author, genre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(CollectionModel.of(books, linkTo(methodOn(BookController.class).searchBooks(title, author, genre)).withSelfRel()), HttpStatus.OK);
    }
}
