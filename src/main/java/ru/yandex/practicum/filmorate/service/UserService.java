package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage userStorage;

    public User createUser(@RequestBody @Valid User user) {
        return userStorage.createUser(user);
    }


    public User updateUser(@RequestBody @Valid User user) {
        return userStorage.updateUser(user);
    }


    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(@PathVariable("id") Integer userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        List<User> users = getUsers();
        User user = findById(userId, users);
        User friend = findById(friendId, users);
        if (!user.getFriends().contains(friend)) {
            user.addFriends(friendId);
            friend.addFriends(userId);
        } else {
            throw new UnsupportedOperationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {

            List<User> users = getUsers();
            User user = findById(userId, users);
            User friend = findById(friendId, users);

            if (user.getFriends().contains(friendId)) {
                user.deleteFriend(friendId);
                friend.deleteFriend(userId);
            } else {
                throw new UnsupportedOperationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
            }

    }

    public List<User> getFriends(Integer userId) {
            List<User> users = getUsers();
            User user = findById(userId, users);
            List<User> friends = new ArrayList<>();
            for (Integer id : user.getFriends()) {
                friends.add(findById(id, users));
            }
            return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {

            List<User> users = getUsers();
            User user = findById(userId, users);
            User friend = findById(friendId, users);
            List<Integer> idCommonFriends = user.getFriends().stream()
                    .distinct()
                    .filter(friend.getFriends()::contains)
                    .collect(Collectors.toList());
            List<User> commonFriends = new ArrayList<>();
            for (Integer id : idCommonFriends) {
                commonFriends.add(findById(id, users));
            }
            return commonFriends;
    }

    private User findById(Integer userId, List<User> users) {
        return users.stream()
                .filter(x -> x.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ValidationException(String.format("Пользователь № %d не найден", userId)));
    }
}
