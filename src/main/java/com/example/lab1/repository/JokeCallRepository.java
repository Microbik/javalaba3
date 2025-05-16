package com.example.lab1.repository;

import com.example.lab1.model.JokeCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JokeCallRepository extends JpaRepository<JokeCall, Long> {
}