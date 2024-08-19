/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String title;
//    private String author;
//    private String genre;
//    private double price;
//    private int quantityAvailable;


    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Quantity available is mandatory")
    @Min(value = 0, message = "Quantity available must be non-negative")
    private int quantityAvailable;

    // Getters and Setters
}
