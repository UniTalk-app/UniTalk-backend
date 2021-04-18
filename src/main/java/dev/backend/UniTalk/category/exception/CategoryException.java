package dev.backend.UniTalk.exception;

public class CategoryException extends RuntimeException
 {
    public CategoryException(Long id)
    {
        super("Could not find category " + id);
    }
}