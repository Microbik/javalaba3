package com.example.lab1.service;

import com.example.lab1.model.Joke;

import java.util.List;
import java.util.Optional;

public interface JokeService {
    List<Joke> getAll();
    Optional<Joke> getById(Long id);
    Joke create(Joke joke);
    Optional<Joke> update(Long id, Joke joke);
    void delete(Long id);
    String getRandomJokeText();
}

