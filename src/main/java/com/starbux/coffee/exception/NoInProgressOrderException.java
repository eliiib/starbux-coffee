package com.starbux.coffee.exception;

import com.starbux.coffee.config.exception.BadRequestException;

public class NoInProgressOrderException extends BadRequestException {

    public NoInProgressOrderException(String msg) {
        super(msg);
    }
}
