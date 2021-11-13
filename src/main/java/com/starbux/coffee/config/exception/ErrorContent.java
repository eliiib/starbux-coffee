package com.starbux.coffee.config.exception;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorContent {
    private String code;
    private String message;
    private List<String> fields;
}
