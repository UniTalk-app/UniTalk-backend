package dev.backend.UniTalk.exception;

public class ThreadException extends RuntimeException {

    public ThreadException(Long id) {
        super("Could not find thread " + id);
    }
}