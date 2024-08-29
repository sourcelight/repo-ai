package ai.example.store.products.controller;

import ai.example.store.products.entities.Product;
import ai.example.store.products.exceptions.ResourceNotFoundException;
import ai.example.store.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ViewController.class)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testListProducts() throws Exception {
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

        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("inventory"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Arrays.asList(product1, product2)));
    }

    @Test
    public void testViewProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productService.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product));
    }

/*    @Test
    public void testViewProductNotFound() throws Exception {
        when(productService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }*/

    @Test
    public void testViewProductNotFound() throws Exception {
        when(productService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Product not found"));
    }


/*    @Test
    public void testViewProductNotFound() throws Exception {
        when(productService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error-404")) // Assuming you have a 404 error page
                .andExpect(model().attributeExists("error"));
    }*/


    @Test
    public void testNewProductForm() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testSaveProduct() throws Exception {
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .param("name", "Product 1")
                        .param("description", "Description 1")
                        .param("price", "10.0")
                        .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    public void testEditProductForm() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productService.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-update"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Existing Product");
        existingProduct.setDescription("Existing Description");
        existingProduct.setPrice(40.0);
        existingProduct.setQuantity(20);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(50.0);
        updatedProduct.setQuantity(25);

        when(productService.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productService.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(post("/products/update/1")
                        .param("name", "Updated Product")
                        .param("description", "Updated Description")
                        .param("price", "50.0")
                        .param("quantity", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(post("/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}