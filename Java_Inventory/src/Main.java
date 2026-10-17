
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args){
        ProductRepository repository = new InMemoryProductRepository();
        InventoryService inventory = new InventoryService(repository);

// Add products, including one without stock.
        inventory.addProduct(new Product(1L, "Sword", 10));
        inventory.addProduct(new Product(2L, "Shield", 0));

// Find an existing product.
        inventory.findById(1L).ifPresent(product ->
                System.out.println("Found: " + product.getName())
        );

// A missing product returns an empty Optional.
        System.out.println(
                "Product 99 exists: " + inventory.findById(99L).isPresent()
        );

// Add stock: Shield goes from 0 to 5.
        inventory.addStock(2L, 5);

// Both products should now be available.
        for (Product product : inventory.getAvailableProducts()) {
            System.out.printf(
                    "%s: %d units%n",
                    product.getName(),
                    product.getStock()
            );
        }

// Duplicate ID.
        try {
            inventory.addProduct(new Product(1L, "Another sword", 3));
        } catch (IllegalArgumentException exception) {
            System.out.println("Expected error: " + exception.getMessage());
        }

// Invalid stock amount.
        try {
            inventory.addStock(1L, -5);
        } catch (IllegalArgumentException exception) {
            System.out.println("Expected error: " + exception.getMessage());
        }

// Missing product.
        try {
            inventory.addStock(99L, 5);
        } catch (IllegalArgumentException exception) {
            System.out.println("Expected error: " + exception.getMessage());
        }

// Remove a product.
        inventory.removeProduct(1L);
        System.out.println(
                "Sword exists after removal: " + inventory.findById(1L).isPresent()
        );

// Removing it again should fail.
        try {
            inventory.removeProduct(1L);
        } catch (IllegalArgumentException exception) {
            System.out.println("Expected error: " + exception.getMessage());
        }
    }
}

