package com.starbux.coffee.exception;

import com.starbux.coffee.config.exception.BadRequestException;

public class NoInProgressOrderException extends BadRequestException {

    public NoInProgressOrderException() {
        super();
    }

    public NoInProgressOrderException(String msg) {
        super(msg);
    }
}
