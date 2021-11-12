package com.starbux.coffee.config.exception;

public class BadRequestException extends RuntimeException {

    private Object[] parameters;

    public Object[] getParameters() {
        return parameters;
    }

    public BadRequestException() {super();}

    public BadRequestException(String msg) {super(msg);}

    public BadRequestException(Object[] parameters) {
        this.parameters = parameters;
    }
}
