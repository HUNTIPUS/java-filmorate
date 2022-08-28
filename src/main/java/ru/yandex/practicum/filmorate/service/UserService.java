package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    public void addFriend(Integer userId, Integer friendId, List<User> users) {
        if (doValidation(userId, friendId)) {
            User user = findById(userId, users);
            User friend = findById(friendId, users);
            if (!user.getFriends().contains(friend)) {
                user.addFriends(friendId);
                friend.addFriends(userId);
            } else {
                throw new ValidationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь/ли не существует/ют.");
        }
    }

    public void deleteFriend(Integer userId, Integer friendId, List<User> users) {
        if (doValidation(userId, friendId)) {
            User user = findById(userId, users);
            User friend = findById(friendId, users);

            if (user.getFriends().contains(friendId)) {
                user.deleteFriend(friendId);
                friend.deleteFriend(userId);
            } else {
                throw new ValidationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь/ли не существует/ют.");
        }
    }

    public List<User> getFriends(Integer userId, List<User> users) {
        if (userId != null && userId > 0) {
            User user = findById(userId, users);
            List<User> friends = new ArrayList<>();
            for (Integer id : user.getFriends()) {
                friends.add(findById(id, users));
            }
            return friends;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не существует.");
        }
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId, List<User> users) {
        if (doValidation(userId, friendId)) {
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
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь/ли не существует/ют.");
        }
    }

    private User findById(Integer userId, List<User> users) {
        return users.stream()
                .filter(x -> x.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ValidationException(String.format("Пользователь № %d не найден", userId)));
    }

    private Boolean doValidation(Integer userId, Integer friendId) {
        if (userId > 0 && friendId > 0
                && userId != null && friendId != null) {
            return true;
        }
        return false;
    }
}
