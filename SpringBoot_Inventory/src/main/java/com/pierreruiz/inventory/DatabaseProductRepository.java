package com.pierreruiz.inventory;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseProductRepository implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    public DatabaseProductRepository(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Product product) {
        jpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(long id){
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(long id){
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll(){
        return jpaRepository.findAll();
    }
}
