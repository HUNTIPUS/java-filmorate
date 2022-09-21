package ru.yandex.practicum.filmorate.exception;

public class ObjectExcistenceException extends RuntimeException{
    public ObjectExcistenceException(final String message) {
        super(message);
    }
}
