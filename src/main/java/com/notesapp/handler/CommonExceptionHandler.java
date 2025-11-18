package com.notesapp.handler;

import com.notesapp.exception.InvalidTagException;
import com.notesapp.exception.NoteNotFoundException;
import com.notesapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({NoteNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<Object> handleInvalidTagException(InvalidTagException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toUnmodifiableList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
