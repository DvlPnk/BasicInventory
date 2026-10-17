package com.pierreruiz.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(
        @Positive(message = "ID must be greater than zero")
        long id,

        @NotBlank(message = "Name must not be blank")
        String name,

        @PositiveOrZero(message = "Stock cannot be negative")
        int stock) {
}
