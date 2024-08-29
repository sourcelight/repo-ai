/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.store.products.controller;

import ai.example.store.products.entities.Product;
import ai.example.store.products.exceptions.ResourceNotFoundException;
import ai.example.store.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ViewController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = {"", "/"})
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "inventory";
    }

    /*@GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"))
        );
        return "product-detail";
    }*/

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        return "product-detail";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        return "product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.findById(id).ifPresent(existingProduct -> {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            productService.save(existingProduct);
        });
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
