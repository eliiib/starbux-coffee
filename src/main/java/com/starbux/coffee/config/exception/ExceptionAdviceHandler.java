package com.starbux.coffee.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceHandler {

    private final MessageSource messageSource;

    @Autowired
    public ExceptionAdviceHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorContent handle(BadRequestException e) {
        return translateException(e, e.getParameters());
    }


    private ErrorContent translateException(Exception e, Object[] parameters) {
        logError(e);
        return parseErrorMessage(messageSource.getMessage(e.getClass().getName(), parameters, Locale.getDefault()));
    }

    private void logError(Exception e) {
        if (e instanceof BadRequestException)
            log.warn("Error Happened", e);

        else
            log.error("Error Happened", e);

        e.printStackTrace();
    }

    private ErrorContent parseErrorMessage(String errorMessage) {
        String[] errorMessageItems = errorMessage.split("#");
        return ErrorContent.builder()
                .message(errorMessageItems[0])
                .code(errorMessageItems[1])
                .fields(Collections.emptyList())
                .build();
    }
}
