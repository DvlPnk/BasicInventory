package com.pierreruiz.inventory;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class InMemoryProductRepository implements ProductRepository
{
    private final Map<Long, Product> products = new HashMap<>();
    @Override
    public void save(Product product) {
        Objects.requireNonNull(product, "Product must not be null.");
        this.products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return products.containsKey(id);
    }

    @Override
    public void deleteById(long id) {
        products.remove(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}
