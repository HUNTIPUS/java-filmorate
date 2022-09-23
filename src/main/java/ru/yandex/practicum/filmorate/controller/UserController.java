package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
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
        if (user != null) {
            if (doValidate(user)) {
                log.info("Пользователь создан");
                return userService.createUser(user);
            }
            return null;
        } else {
            throw new ValidationException("Передано пустое значение");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (user != null && user.getId() > 0) {
            if (doValidate(user)) {
                log.info("Пользователь обновился");
                return userService.updateUser(user);
            }
            return null;
        } else {
            throw new ObjectExcistenceException("Пользователь не существует.");
        }
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Вывод всех созданных пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        log.info("Пользователь с id = " + userId);
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        log.info("Добавление в друзья");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId,
                             @PathVariable("friendId") Integer friendId) {
        log.info("Удаление из друзей");
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer userId) {
        log.info("Показ всех друзей пользователя с id = " + userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer userId,
                                       @PathVariable("otherId") Integer friendId) {
        log.info("Список общих друзей");
        return userService.getCommonFriends(userId, friendId);
    }

    private Boolean doValidate(User user) {
        if (!user.getLogin().contains(" ")) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return true;
        }
        throw new ValidationException("Ошибка валидации");
    }
}
