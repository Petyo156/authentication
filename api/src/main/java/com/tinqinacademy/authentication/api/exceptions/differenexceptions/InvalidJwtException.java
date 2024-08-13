package com.tinqinacademy.authentication.api.exceptions.differenexceptions;

public class InvalidJwtException extends CustomException{
    public InvalidJwtException(String message) {
        super(message);
    }
}
