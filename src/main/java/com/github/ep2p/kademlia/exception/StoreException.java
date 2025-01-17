package com.github.ep2p.kademlia.exception;

public class StoreException extends Exception {
    public StoreException() {
        super("Failed to store key value");
    }

    public StoreException(String message) {
        super(message);
    }

    public StoreException(Exception e) {
        super(e);
    }
}
