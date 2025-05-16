package com.example.lab1.service;

import com.example.lab1.model.Joke;
import com.example.lab1.model.JokeCall;
import com.example.lab1.repository.JokeRepository;
import com.example.lab1.repository.JokeCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;
    private final JokeCallRepository jokeCallRepository;

    @Override
    public Page<Joke> getAll(Pageable pageable) {
        return jokeRepository.findAll(pageable);
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
        return jokeRepository.findRandomJoke()
                .map(Joke::getText)
                .orElse("Нет доступных шуток");
    }

    @Override
    public Optional<Long> getRandomJokeId() {
        return jokeRepository.findRandomJoke()
                .map(Joke::getId);
    }

    @Override
    public List<Joke> getTopJokes(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jokeRepository.findTopJokes(pageable);
    }

    @Override
    @Transactional
    public void recordJokeCall(Long jokeId, Long userId) {
        JokeCall call = new JokeCall();
        call.setJoke(jokeRepository.getReferenceById(jokeId));
        call.setUserId(userId);
        call.setCallTime(LocalDateTime.now());
        jokeCallRepository.save(call);
    }

}
