package ru.practicum.main_service.excemptions;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }
}
