package com.starbux.coffee.exception;

public class NoInProgressOrderException extends RuntimeException {

    public NoInProgressOrderException(String msg) {
        super(msg);
    }
}
