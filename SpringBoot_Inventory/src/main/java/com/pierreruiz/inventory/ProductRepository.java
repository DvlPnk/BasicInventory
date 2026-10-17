package com.pierreruiz.inventory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
{
    void save(Product product);
    Optional<Product> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    List<Product> findAll();
}
