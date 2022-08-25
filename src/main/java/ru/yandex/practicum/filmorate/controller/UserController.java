package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.generate.GenerateIdUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private GenerateIdUser generateIdUser = new GenerateIdUser();

    @PostMapping
    public User createUser(@RequestBody @NotNull User user) {
        if (!user.getEmail().isEmpty() && user.getEmail().contains("@")
                && !user.getLogin().isEmpty() && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now())) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(generateIdUser.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Не удалось добавить пользователя");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @NotNull User user) {
        if (!user.getEmail().isEmpty() && user.getEmail().contains("@")
                && !user.getLogin().isEmpty() && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now())
                && user.getId() > 0) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            for (Integer id: users.keySet()) {
                if (id == user.getId()) {
                    users.put(user.getId(), user);
                }
            }
            return user;
        } else {
            throw new ValidationException("Не удалось обновить пользователя");
        }
    }

    @GetMapping
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
