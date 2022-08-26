package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private GenerateId generateId = new GenerateId();

    @PostMapping
    public User createUser(@RequestBody @Valid @NotNull User user) {
        try {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(generateId.getId());
            users.put(user.getId(), user);
            return user;
        } catch (ValidationException e){
            throw new ValidationException("Не удалось добавить пользователя");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid @NotNull User user) {
        if (user.getId() > 0) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            for (Integer id : users.keySet()) {
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
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private Boolean doValidation(LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }
}
