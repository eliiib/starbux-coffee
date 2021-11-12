package com.starbux.coffee.exception;

import com.starbux.coffee.config.exception.BadRequestException;

public class ProductNotFoundException extends BadRequestException {

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String msg) {
        super(msg);
    }

}
