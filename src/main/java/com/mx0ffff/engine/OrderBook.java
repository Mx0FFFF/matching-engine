package com.mx0ffff.engine;

import java.time.Instant;
import java.util.*;

public class OrderBook {
    private final NavigableMap<Long, Queue<Order>> bids;
    private final NavigableMap<Long, Queue<Order>> asks;
    private final String symbol;

    public OrderBook (String symbol) {
        this.symbol = symbol;
        this.bids = new TreeMap<>(Comparator.reverseOrder());
        this.asks = new TreeMap<>();
    }

    private boolean crosses(Order incoming, long restingPrice) {
        if (incoming.getSide() == Side.BUY) {
            return incoming.getPrice() >= restingPrice;
        }
        return incoming.getPrice() <= restingPrice;
    }

    public List<Trade> submitOrder(Order order) {
        //Find best bid from either side
        //Take the best order, decide the quantity and price, reduce quantity on both and the build a Trade

        NavigableMap<Long, Queue<Order>> opposite = (order.getSide() == Side.BUY) ? asks : bids;
        List<Trade> trades = new ArrayList<>();
        while (order.getQuantity() > 0 && !opposite.isEmpty() && crosses(order, opposite.firstKey())) {
            long bestPrice = opposite.firstKey();
            Queue<Order> level = opposite.get(bestPrice);
            Order resting = level.peek();
            int tradeQuantity = Math.min(order.getQuantity(), resting.getQuantity());
            long tradePrice = bestPrice;
            order.reduceQuantity(tradeQuantity);
            resting.reduceQuantity(tradeQuantity);
            String buyOrderId = (order.getSide() == Side.BUY) ? order.getId() : resting.getId();
            String sellOrderId = (order.getSide() == Side.SELL) ? order.getId() : resting.getId();

            trades.add(new Trade(
                    UUID.randomUUID().toString(),
                    buyOrderId,
                    sellOrderId,
                    resting.getId(),
                    order.getId(),
                    symbol,
                    tradeQuantity,
                    tradePrice,
                    Instant.now()
            ));


            if (resting.getQuantity() == 0) {
                level.poll();
                if (level.isEmpty()) {
                    opposite.remove(bestPrice);
                }
            }

        }
        return trades;
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
        bids.values().removeIf(Queue::isEmpty);
        asks.values().removeIf(Queue::isEmpty);
        return removed;

    }

    public Optional<Long> bestBid() {
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
