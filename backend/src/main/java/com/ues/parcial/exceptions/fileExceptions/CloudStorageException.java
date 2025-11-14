package com.ues.parcial.exceptions.fileExceptions;

public class CloudStorageException extends RuntimeException {
    public CloudStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}