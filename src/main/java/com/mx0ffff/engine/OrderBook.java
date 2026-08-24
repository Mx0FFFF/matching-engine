package com.mx0ffff.engine;

import java.util.*;

public class OrderBook {
    private final NavigableMap<Long, Queue<Order>> bids;
    private final NavigableMap<Long, Queue<Order>> asks;
    private final String symbol;

    public OrderBook (String symbol) {
        this.symbol = symbol;
        this.bids = new Treemap<>(Comparator.reverseOrder());
        this.asks = new Treemap<>();
    }

    public List<Trade> submitOrder(Order order) {

    }

    public boolean cancelOrder(String orderId) {
        boolean removed = false;

        for (Queue<Order> level : bids.values()) {
            if (level.removeIf(order -> order.getId().equals(orderId))) {
                removed = true;
            }
        }

        for (Queue<Order> level : asks.values()) {
            if (level.removeIf(order-> order.getId().equals(orderId))) {
                removed = true;
            }
        }
        return removed;

    }

    public Optional<Long> bestBid() {
        // if map = empty return emtpy Optional. else wrap bids.firstkey() in an optional
        if (bids.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bids.firstKey());
    }

    public Optional<Long> bestAsk() {
        if (asks.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(asks.firstKey());
    }

}
