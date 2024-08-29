/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.store.products.repositories;

import ai.example.store.products.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}