package com.mx0ffff.engine;
import java.time.Instant;

public class Order {
    // An order contains an identifier, type, quantity, price and a timestamp.
    private final String id;
    private final Side side;
    private int quantity;
    // Cents
    private final long price;
    private final Instant timestamp;

    // Constructor
    public Order (String id, Side side, int quantity, long price, Instant timestamp) {
        this.id = id;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    // method for updating the quantity of the orders
    public void reduceQuantity (int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot reduce by " + amount + " because it's negative or zero");
        }

        if (amount > quantity) {
            throw new IllegalArgumentException("Cannot reduce because " + amount + " is larger than " + quantity);
        }
        this.quantity -= amount;
    }

    // Getters
    public String getId () {
        return this.id;
    }
    public Side getSide () {
        return this.side;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public long getPrice () {
        return this.price;
    }
    public Instant getTimestamp () {
        return this.timestamp;
    }

}
