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

    @Test
    // Vandring over to prisnivåer. A1 (80 @ 10100), A2 (150 @ 10200), så BUY 200 @ 10300 → to handler, til 10100 og 10200. Verifiserer prisforbedring og at nivået slettes.
    void buyWalksTwoPriceLevels () {
        OrderBook book = new OrderBook("GMC");
        book.submitOrder(new Order("A1",Side.SELL, 80, 10100L, Instant.now()));
        book.submitOrder(new Order("A2",Side.SELL, 150, 10200L, Instant.now()));
        List<Trade> trades = book.submitOrder(new Order("B1", Side.BUY, 200, 10300L, Instant.now()));
        assertEquals(80, trades.get(0).quantity());
        assertEquals(120, trades.get(1).quantity());
        assertEquals(Optional.of(10200L) ,book.bestAsk());
        assertEquals(10100L, trades.get(0).price());
        assertEquals(10200L, trades.get(1).price());
    }

    @Test
    void buyWalksTwoRestingAsks() {
        OrderBook book = new OrderBook("SSNLF");
        book.submitOrder(new Order("S1", Side.SELL, 100, 9900L, Instant.now()));
        book.submitOrder(new Order("S2", Side.SELL, 50, 9900L, Instant.now()));
        List<Trade> trades = book.submitOrder(new Order("B1", Side.BUY, 120, 9900L, Instant.now()));
        assertEquals(2, trades.size());
        assertEquals(100, trades.get(0).quantity());
        assertEquals(20, trades.get(1).quantity());
        assertEquals(Optional.of(9900L), book.bestAsk());
    }
    @Test
    // Create a test to check if canceling a sell order work
    void cancelSellOrder() {
        OrderBook book = new OrderBook("AMC");
        book.submitOrder(new Order("A1", Side.SELL, 200, 8900L, Instant.now()));
        assertTrue(book.cancelOrder("A1"));
        assertEquals(Optional.empty(), book.bestAsk());
    }

    @Test
    void nonCrossingBuyOrder() {
        OrderBook book = new OrderBook("SPX");
        List<Trade> trades = book.submitOrder(new Order("B1", Side.BUY, 120, 9900L, Instant.now()));
        assertTrue(trades.isEmpty());
        assertEquals(Optional.of(9900L), book.bestBid());
    }

    @Test
    void zeroPriceOrder() {
        assertThrows(IllegalArgumentException.class, () -> new Order("B1", Side.BUY, 120,0L, Instant.now()));
    }

    @Test
    void belowZeroPriceOrder() {
        assertThrows(IllegalArgumentException.class, () -> new Order("B1", Side.BUY, 120, -20L, Instant.now()));
    }
}