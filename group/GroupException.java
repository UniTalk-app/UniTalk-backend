package dev.backend.UniTalk.group;

public class GroupException extends RuntimeException {
    public GroupException(Long id) {
        super("Could not find group " + id);
    }
}