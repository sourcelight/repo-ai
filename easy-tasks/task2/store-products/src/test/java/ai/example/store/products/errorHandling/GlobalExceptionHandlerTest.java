package ai.example.store.products.errorHandling;

import ai.example.store.products.controller.ViewController;
import ai.example.store.products.exceptions.ResourceNotFoundException;
import ai.example.store.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ViewController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testHandleResourceNotFoundException() throws Exception {
        when(productService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Product not found"));
    }

    @Test
    public void testHandleGenericException() throws Exception {
        when(productService.findById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-generic"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Unexpected error"));
    }
}