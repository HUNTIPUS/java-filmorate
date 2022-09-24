package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

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

    public User(int user_id,
                @Email @NotNull String email,
                @NotBlank String login,
                String name,
                @NotNull @PastOrPresent LocalDate birthday) {
        this.id = user_id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}
