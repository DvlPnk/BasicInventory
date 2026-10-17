package com.pierreruiz.inventory;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(long id){
        super("Product with id {" + id + "} already exists");
    }
}
