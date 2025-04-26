package com.example.lab1.service;

import com.example.lab1.model.Joke;
import com.example.lab1.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;

    @Override
    public List<Joke> getAll() {
        return jokeRepository.findAll();
    }

    @Override
    public Optional<Joke> getById(Long id) {
        return jokeRepository.findById(id);
    }

    @Override
    public Joke create(Joke joke) {
        joke.setCreatedAt(LocalDateTime.now());
        return jokeRepository.save(joke);
    }

    @Override
    public Optional<Joke> update(Long id, Joke joke) {
        return jokeRepository.findById(id).map(existing -> {
            existing.setText(joke.getText());
            existing.setUpdatedAt(LocalDateTime.now());
            return jokeRepository.save(existing);
        });
    }

    @Override
    public void delete(Long id) {
        jokeRepository.deleteById(id);
    }

    @Override
    public String getRandomJokeText() {
        List<Joke> jokes = jokeRepository.findAll();
        if (jokes.isEmpty()) {
            return "Нет анекдотов 😢";
        }
        Joke randomJoke = jokes.get(new Random().nextInt(jokes.size()));
        return randomJoke.getText();
    }
}
