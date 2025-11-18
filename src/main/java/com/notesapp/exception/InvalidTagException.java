package com.notesapp.exception;

public class InvalidTagException extends RuntimeException {
    public InvalidTagException(String message) {
        super(message);
    }
}
