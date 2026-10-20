package com.pierreruiz.inventory;

public class StockOverflowException extends RuntimeException{
    public StockOverflowException(String message){
        super(message);
    }
}
