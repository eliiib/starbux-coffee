package com.starbux.coffee.exception;

import com.starbux.coffee.config.exception.BadRequestException;

public class ToppingNotFoundException extends BadRequestException {

    public ToppingNotFoundException() {
        super();
    }

    public ToppingNotFoundException(String msg) {
        super(msg);
    }
}
