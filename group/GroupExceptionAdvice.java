package dev.backend.UniTalk.group;

import dev.backend.UniTalk.group.GroupException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GroupExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(GroupException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)       // 404
    public String groupHandler(GroupException ex) {
        return ex.getMessage();
    }
}
