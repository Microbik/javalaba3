package com.example.lab1.repository;

import com.example.lab1.model.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface JokeRepository extends JpaRepository<Joke, Long> {
    @Query(value = "SELECT * FROM joke ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Joke> findRandomJoke();  // Изменили возвращаемый тип

    @Query("SELECT j FROM Joke j LEFT JOIN j.calls c GROUP BY j.id ORDER BY COUNT(c.id) DESC")
    List<Joke> findTopJokes(Pageable pageable);  // Уже правильно объявлен
}
