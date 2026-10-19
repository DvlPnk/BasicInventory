package com.pierreruiz.inventory;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(long id){
        super("Insufficient stock for product with id: " + id);
    }
}
