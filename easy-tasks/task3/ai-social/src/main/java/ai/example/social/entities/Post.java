/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.social.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    // Getters and Setters
}