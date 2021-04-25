package dev.backend.UniTalk.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodNotAllowedException.class)
//    public ResponseEntity<ErrorMessage> methodNotAllowedException(
//            MethodNotAllowedException ex, WebRequest request) {
//        ErrorMessage message = new ErrorMessage(
//                HttpStatus.METHOD_NOT_ALLOWED.value(),
//                new Date(),
//                "Wrong page. Go back!",
//                request.getDescription(false));
//
//        return new ResponseEntity<ErrorMessage>(message, HttpStatus.METHOD_NOT_ALLOWED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {

        String usefulMsg = ex.getCause().getCause().getMessage();
        int index = usefulMsg.indexOf("\n");
        if (index != -1)
            usefulMsg= usefulMsg.substring(0 , index);

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                usefulMsg,
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
