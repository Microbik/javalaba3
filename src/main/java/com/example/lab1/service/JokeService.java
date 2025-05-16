package com.example.lab1.service;

import com.example.lab1.model.Joke;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface JokeService {
    Page<Joke> getAll(Pageable pageable);
    Optional<Joke> getById(Long id);
    Joke create(Joke joke);
    Optional<Joke> update(Long id, Joke joke);
    void delete(Long id);
    String getRandomJokeText();
    List<Joke> getTopJokes(int limit);
    void recordJokeCall(Long jokeId, Long userId);
    Optional<Long> getRandomJokeId();

}

