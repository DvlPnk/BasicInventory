package com.pierreruiz.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InventoryServiceTest {
    private InventoryService inventory;

    @BeforeEach
    void setUp() {
        inventory = new InventoryService(new InMemoryProductRepository());
    }

    @Test
    void addingProductMakesItFindable() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        Product found = inventory.findById(1L).orElseThrow();

        assertEquals("Product 1", found.getName());
        assertEquals(10, found.getStock());
    }

    @Test
    void duplicateIdIsRejectedWithoutReplacingOriginal() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        assertThrows(
                DuplicateProductException.class,
                () -> inventory.addProduct(new Product(1L, "Product 2", 5))
        );

        assertEquals(
                "Product 1",
                inventory.findById(1L).orElseThrow().getName()
        );
    }

    @Test
    void addingStockIncreasesExistingStock() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        inventory.addStock(1L, 5);

        assertEquals(15, inventory.findById(1L).orElseThrow().getStock());
    }

    @Test
    void invalidStockAmountLeavesStockUnchanged() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> inventory.addStock(1L, -5)
        );

        assertEquals(10, inventory.findById(1L).orElseThrow().getStock());
    }

    @Test
    void addingStockToMissingProductIsRejected() {
        assertThrows(
                ProductNotFoundException.class,
                () -> inventory.addStock(999L, 5)
        );
    }

    @Test
    void availableProductsExcludeZeroStock() {
        inventory.addProduct(new Product(1L, "Product 1", 10));
        inventory.addProduct(new Product(2L, "Product 2", 0));

        var available = inventory.getAvailableProducts();

        assertEquals(1, available.size());
        assertEquals(1L, available.getFirst().getId());
    }

    @Test
    void removedProductIsNoLongerFindable() {
        inventory.addProduct(new Product(1L, "Product 1", 10));

        inventory.removeProduct(1L);

        assertTrue(inventory.findById(1L).isEmpty());
    }
}
