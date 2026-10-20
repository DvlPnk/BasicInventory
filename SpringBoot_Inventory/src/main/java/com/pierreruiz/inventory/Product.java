package com.pierreruiz.inventory;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "products")
public class Product{
    @Id
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int stock;

    protected Product(){
    }

    public Product(long id, String name, int stock){
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than zero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name must not be null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
        this.stock = stock;
    }

    public long getId() {return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }

    public void addStock(int amount){
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        try{
            this.stock = Math.addExact(this.stock, amount);
        }
        catch (ArithmeticException e){
            throw new StockOverflowException("Stock limit exceeded");
        }
    }

    public void removeStock(int amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if(stock < amount){
            throw new InsufficientStockException(id);
        }

        this.stock = Math.subtractExact(this.stock, amount);
    }
}
