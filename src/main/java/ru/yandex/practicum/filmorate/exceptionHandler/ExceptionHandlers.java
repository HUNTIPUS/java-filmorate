package ru.yandex.practicum.filmorate.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<String> exc(ValidationException ex) {
        log.info("Код ошибки: 400 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> exc(ObjectExcistenceException ex) {
        log.info("Код ошибки: 404 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> exc(Throwable ex) {
        log.info("Код ошибки: 500 {}", ex.getMessage());
        return new ResponseEntity<>("Поймано необработанное исключение", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> exc(ConstraintViolationException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> exc(MethodArgumentNotValidException ex){
        log.info("Код ошибки: 400 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}