package com.ues.parcial.exceptions;

public class PhoneAlreadyInUseException extends RuntimeException {
    public PhoneAlreadyInUseException(String message) {
        super(message);
    }
}
