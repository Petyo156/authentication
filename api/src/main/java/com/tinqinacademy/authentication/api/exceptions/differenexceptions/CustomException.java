package com.tinqinacademy.authentication.api.exceptions.differenexceptions;

public abstract class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}