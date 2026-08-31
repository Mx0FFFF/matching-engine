package com.mx0ffff.engine;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class OrderBookTest {
    @Test
    void sellWalksTwoRestingBids() {
        OrderBook book = new OrderBook("AAPL");
        book.submitOrder(new Order("B1", Side.BUY, 100, 9900L, Instant.now()));
        book.submitOrder(new Order("B2", Side.BUY, 50, 9900L, Instant.now()));
        List<Trade> trades = book.submitOrder(new Order("S1", Side.SELL, 120, 9900L, Instant.now()));
        assertEquals(2, trades.size());
        assertEquals(100, trades.get(0).quantity());
        assertEquals(20, trades.get(1).quantity());
        assertEquals(Optional.of(9900L), book.bestBid());
    }
}
