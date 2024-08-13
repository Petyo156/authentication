package com.tinqinacademy.authentication.api.exceptions;

public class InvalidJwtException extends Throwable {

    private String message;

    public InvalidJwtException(String message) {
        this.message = message;
    }
}
