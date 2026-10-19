package com.pierreruiz.inventory;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
public class InventoryService{
    private final ProductRepository repository;

    public InventoryService(ProductRepository repository){
        this.repository = Objects.requireNonNull(repository, "Product repository must not be null.");
    }

    public void addProduct(Product product){
        Objects.requireNonNull(product, "Product must not be null.");

        if (repository.existsById(product.getId())) {
            throw new DuplicateProductException(product.getId());
        }

        repository.save(product);
    }

    public Optional<Product> findById(long id){
        return repository.findById(id);
    }

    public void removeProduct(long id){
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        repository.deleteById(id);
    }

    public List<Product> getAvailableProducts(){
        List<Product> products = new ArrayList<>();
        for(Product product : repository.findAll()){
            if(product.getStock() > 0){
                products.add(product);
            }
        }
        return products;
    }

    @Transactional
    public void addStock(long productId, int amount) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.addStock(amount);
    }

    @Transactional
    public void removeStock(long productId, int amount){
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.removeStock(amount);
    }
}
