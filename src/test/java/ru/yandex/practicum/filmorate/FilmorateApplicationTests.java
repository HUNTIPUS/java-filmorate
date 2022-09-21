package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {


	private final UserDbStorage userDbStorage;
	private final FilmDbStorage filmDbStorage;

	@Test
	public void testUpdateUser() {
		User user = new User(1, "ghbgh@mail.ru",
				"ghbghgb", "ghbgh", LocalDate.of(2015, 12, 11));

		userDbStorage.createUser(user);

		User newUser = new User(1, "ghbgh@mail.ru",
				"ffff", "ffff", LocalDate.of(2015, 12, 11));

		userDbStorage.updateUser(newUser);
		assertEquals(newUser, userDbStorage.getUserById(1).orElseThrow(() -> new ObjectExcistenceException("-")));
	}

	@Test
	public void testGetUserById() {

		User newUser = new User(1, "ghbgh@mail.ru",
				"ghbghgb", "ghbgh", LocalDate.of(2015, 12, 11));

		userDbStorage.createUser(newUser);

		Optional<User> userOptional = userDbStorage.getUserById(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testGetUsers() {
		User user = new User(1, "ghbgh@mail.ru",
				"ffff", "ffff", LocalDate.of(2015, 12, 11));

		List<User> users = new ArrayList<>();
		users.add(user);

		userDbStorage.createUser(user);
		assertEquals(users, userDbStorage.getUsers());
	}

	@Test
	public void testGetCommonFriends() {
		User user = new User(1, "ghbgh@mail.ru",
				"ffff", "ffff", LocalDate.of(2015, 12, 11));

		User newUser = new User(2, "ghbgh@mail.ru",
				"ffff", "ffff", LocalDate.of(2015, 12, 11));

		User newUser3 = new User(3, "ghbgh@mail.ru",
				"ffff", "ffff", LocalDate.of(2015, 12, 11));

		userDbStorage.createUser(user);
		userDbStorage.createUser(newUser);

		userDbStorage.addFriend(1, 2, 2);
		userDbStorage.addFriend(1, 3, 2);
		userDbStorage.addFriend(2, 3, 2);

		assertEquals(newUser3, userDbStorage.getCommonFriends(1, 2).get(0));
	}

	@Test
	public void testUpdateFilm() {
		Mpa mpa = new Mpa(1, "G");
		Film film = new Film(1, "hgbg",
				"ghbghb", LocalDate.of(2010, 1, 1), 120, mpa);

		filmDbStorage.createFilm(film);

		Film newFilm = new Film(1, "fffff",
				"fffff", LocalDate.of(2010, 1, 1), 120, mpa);

		filmDbStorage.updateFilm(newFilm);

		assertEquals(newFilm, filmDbStorage.getFilmById(1)
				.orElseThrow(() -> new ObjectExcistenceException("-")));
	}

	@Test
	public void testGetFilmById() {
		Mpa mpa = new Mpa(1, "G");
		Film newFilm = new Film(1, "hgbg",
				"ghbghb", LocalDate.of(2010, 1, 1), 120, mpa);

		filmDbStorage.createFilm(newFilm);

		Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testGetFilms() {

		Mpa mpa = new Mpa(1, "G");
		Film newFilm = new Film(1, "hgbg",
				"ghbghb", LocalDate.of(2010, 1, 1), 120, mpa);

		filmDbStorage.createFilm(newFilm);

		List<Film> films = new ArrayList<>();
		films.add(newFilm);

		assertEquals(films, filmDbStorage.getFilms());
	}
}
