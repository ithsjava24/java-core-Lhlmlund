package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;


public record ProductRecord(UUID uuid, String name, Category category, BigDecimal price) {

    public ProductRecord withPrice(BigDecimal newPrice) {
        return new ProductRecord(uuid, name, category, newPrice);
    }







}
