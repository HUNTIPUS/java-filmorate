package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        if (doValidate(user)) {
            return userService.createUser(user);
        } else {
            throw new ValidationException("Не удалось создать пользователя");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (user.getId() > 0 && user.getId() != null) {
            if (doValidate(user)) {
                return userService.updateUser(user);
            } else {
                throw new ValidationException("В логине пользоватля есть пробелы");
            }
        } else {
            throw new NullPointerException("Пользователь с таким id не существует");
        }
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        if (userId > 0 && userId != null) {
            return userService.getUserById(userId);
        } else {
            throw new NullPointerException("Пользователь с таким id не существует");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        if (checkForExistence(userId, friendId)) {
            userService.addFriend(userId, friendId);
        } else {
            throw new NullPointerException("Пользователь/ли не существует/ют.");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId,
                             @PathVariable("friendId") Integer friendId) {
        if (checkForExistence(userId, friendId)) {
            userService.deleteFriend(userId, friendId);
        } else {
            throw new NullPointerException("Пользователь/ли не существует/ют.");
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer userId) {
        if (userId != null && userId > 0) {
            return userService.getFriends(userId);
        } else {
            throw new NullPointerException("Пользователь не существует.");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer userId,
                                       @PathVariable("otherId") Integer friendId) {
        if (checkForExistence(userId, friendId)) {
            return userService.getCommonFriends(userId, friendId);
        } else {
            throw new NullPointerException("Пользователь/ли не существует/ют.");
        }
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> exc(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> exc(NullPointerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UnsupportedOperationException .class)
    public ResponseEntity<String> exc(UnsupportedOperationException  ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Boolean doValidate(User user) {
        if (!user.getLogin().contains(" ")) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return true;
        }
        return false;
    }

    private Boolean checkForExistence(Integer userId, Integer friendId) {
        if (userId > 0 && friendId > 0
                && userId != null && friendId != null) {
            return true;
        }
        return false;
    }
}
