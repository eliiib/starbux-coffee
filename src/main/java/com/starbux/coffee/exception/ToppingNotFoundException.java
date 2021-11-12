package com.starbux.coffee.exception;

public class ToppingNotFoundException extends RuntimeException {

    public ToppingNotFoundException(String msg) {
        super(msg);
    }
}
