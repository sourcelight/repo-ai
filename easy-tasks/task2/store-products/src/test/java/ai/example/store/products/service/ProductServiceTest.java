package ai.example.store.products.service;

import ai.example.store.products.entities.Product;
import ai.example.store.products.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);
        product1.setQuantity(5);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);
        product2.setQuantity(10);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.findAll();
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        assertEquals("Product 2", products.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);
        assertEquals("Product 1", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testFindById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.findById(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals("Product 1", foundProduct.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}