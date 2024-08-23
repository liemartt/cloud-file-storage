package com.liemartt.cloud.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ErrorParser {
    public static String parseError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Internal error");
    }
}
