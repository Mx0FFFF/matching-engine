package com.mx0ffff.engine;

import java.time.Instant;

public record Trade(String id, String buyOrderId, String sellOrderId,
                    String makerOrderId, String takerOrderId, String symbol,
                    int quantity, long price, Instant timestamp) {

    public Trade {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive, got: " + quantity);
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive, got: " + price);
        }
    }
}