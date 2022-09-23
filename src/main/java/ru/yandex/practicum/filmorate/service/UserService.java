package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userDbStorage;
    private final FriendStorage friendStorage;

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }


    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }


    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        return userDbStorage.getUserById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (!friendStorage.getFriends(userId).contains(friend) &&
                !friendStorage.getFriends(friendId).contains(user)) {
            friendStorage.addFriend(userId, friendId, 2);
            friendStorage.addFriend(friendId, userId, 1);
        } else if (!friendStorage.getFriends(userId).contains(friend) &&
                friendStorage.getFriends(friendId).contains(user)) {
            friendStorage.updateStatusFriendship(userId, friendId, 2);
        } else if (friendStorage.getFriends(userId).contains(friend)) {
            throw new ValidationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (friendStorage.getFriends(userId).contains(friend) &&
                friendStorage.getFriends(friendId).contains(user)) {
            friendStorage.updateStatusFriendship(userId, friendId, 1);
        } else if (friendStorage.getFriends(userId).contains(friend) &&
                !friendStorage.getFriends(friendId).contains(user)) {
            friendStorage.deleteFriend(userId, friendId);
            friendStorage.deleteFriend(friendId, userId);
        } else if (!friendStorage.getFriends(userId).contains(friend)) {
            throw new ValidationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
        }

    }

    public List<User> getFriends(Integer userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
