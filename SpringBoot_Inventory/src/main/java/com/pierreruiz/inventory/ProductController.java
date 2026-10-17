package com.pierreruiz.inventory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final InventoryService inventoryService;

    public ProductController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public record AddStockRequest(
            @Positive(message = "Amount must be greater than zero")
            int amount){
    }

    @GetMapping("/available")
    public List<Product> getAvailableProducts() {
        return inventoryService.getAvailableProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") long id) {
        return inventoryService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id)
                );
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = new Product(
                request.id(),
                request.name(),
                request.stock()
        );

        inventoryService.addProduct(product);
        return ResponseEntity.created(URI.create("/products/" + product.getId())).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id){
        inventoryService.removeProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<Void> addStock(@PathVariable("id") long id,
                                         @Valid @RequestBody AddStockRequest request){
        inventoryService.addStock(id, request.amount());
        return ResponseEntity.noContent().build();
    }
}
