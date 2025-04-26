package com.example.lab1.repository;

import com.example.lab1.model.Joke;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JokeRepository extends JpaRepository<Joke, Long> {
}
