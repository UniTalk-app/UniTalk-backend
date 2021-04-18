package dev.backend.UniTalk.category;

public class CategoryException extends RuntimeException
 {
    public CategoryException(Long id)
    {
        super("Could not find category " + id);
    }
}