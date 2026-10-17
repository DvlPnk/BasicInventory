package com.pierreruiz.inventory;

public record CreateProductRequest(long id, String name, int stock) {
}
