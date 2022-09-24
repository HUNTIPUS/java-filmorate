package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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

    @NotNull
    private Mpa mpa;

    public Film(int film_id,
                @NotBlank String title,
                @Size(max = 200) String description,
                @NotNull LocalDate release_date,
                @Positive int duration,
                @NotNull Mpa mpa) {
        this.id = film_id;
        this.name = title;
        this.description = description;
        this.releaseDate = release_date;
        this.duration = duration;
        this.mpa = mpa;
    }
}
