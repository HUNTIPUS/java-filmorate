package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    @Email
    @NotNull
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    private List<Integer> friends = new ArrayList<>();

    public User(int user_id, String email, String login, String name, LocalDate birthday) {
        this.id = user_id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public void addFriends(Integer friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Integer friendId) {
        friends.remove(friendId);
    }

}
