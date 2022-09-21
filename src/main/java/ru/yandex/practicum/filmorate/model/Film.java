package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;

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

    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;

    private List<User> usersWhichLikeFilm = new ArrayList<>();

    public Film(int film_id,
                String title,
                String description,
                LocalDate release_date,
                int duration,
                Mpa mpa) {
        this.id = film_id;
        this.name = title;
        this.description = description;
        this.releaseDate = release_date;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addLikes(User user) {
        countLikes++;
        usersWhichLikeFilm.add(user);
    }

    public void deleteLikes(Integer idUser) {
        countLikes--;
        usersWhichLikeFilm.remove(idUser);
    }
}
