package dev.backend.UniTalk.thread;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ThreadExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(ThreadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)       // 404
    public String threadHandler(ThreadException ex) {
        return ex.getMessage();
    }
}
