package dev.backend.UniTalk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CategoryExceptionAdvice
{
    @ResponseBody
    @ExceptionHandler(CategoryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)       // 404
    public String CategoryHandler(CategoryException ex)
    {
        return ex.getMessage();
    }
}