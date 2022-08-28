package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        return userStorage.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        userService.addFriend(userId, friendId, userStorage.getUsers());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId,
                             @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(userId, friendId, userStorage.getUsers());
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer userId) {
        return userService.getFriends(userId, userStorage.getUsers());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer userId,
                                       @PathVariable("otherId") Integer friendId) {
        return userService.getCommonFriends(userId, friendId, userStorage.getUsers());
    }
}
