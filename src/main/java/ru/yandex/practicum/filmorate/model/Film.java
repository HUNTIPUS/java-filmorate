package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private long duration;

    @Min(0)
    private Integer countLikes = 0;

    private List<Integer> usersWhichLikeFilm = new ArrayList<>();

    public void addLikes() {
        countLikes++;
    }
    public void addUsersWhichLikeFilm(Integer idUser) {
        usersWhichLikeFilm.add(idUser);
    }

    public void deleteLikes() {
        countLikes--;
    }

    public void deleteLikeByUser(Integer idUser) {
        usersWhichLikeFilm.remove(idUser);
    }
}
