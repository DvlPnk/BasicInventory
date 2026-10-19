package com.pierreruiz.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:inventory-tests",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class InventoryPersistenceTest {

    @Autowired
    private InventoryService inventory;

    @Autowired
    private JpaProductRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    void productIsStoredInDatabase() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        Product stored = repository.findById(1L).orElseThrow();

        assertEquals("Product 1", stored.getName());
        assertEquals(10, stored.getStock());
    }

    @Test
    void stockChangesArePersisted() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        inventory.addStock(1L, 5);

        Product reloaded = repository.findById(1L).orElseThrow();
        assertEquals(15, reloaded.getStock());
    }

    @Test
    void productIsDeletedFromDatabase() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        inventory.removeProduct(1L);

        assertFalse(repository.existsById(1L));
    }
}
