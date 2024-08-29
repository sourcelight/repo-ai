package ai.example.store.products.service;

import ai.example.store.products.entities.Product;
import ai.example.store.products.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testFindAll() {
        Product product = Product.builder().name("Product1").description("Description1").price(10.0).quantity(5).build();
        List<Product> products = Arrays.asList(product);
        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.findAll();
        assertEquals(1, result.size());
    }

    // Additional test cases...
}