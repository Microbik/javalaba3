package com.example.lab1.controller;

import com.example.lab1.model.Joke;
import com.example.lab1.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokeController {

    private final JokeService jokeService;

    @GetMapping
    public ResponseEntity<Page<Joke>> getAllJokes(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(jokeService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joke> getById(@PathVariable Long id) {
        return jokeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/top")
    public List<Joke> getTopJokes() {
        return jokeService.getTopJokes(5);
    }

    @PostMapping
    public Joke create(@RequestBody Joke joke) {
        return jokeService.create(joke);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joke> update(@PathVariable Long id, @RequestBody Joke joke) {
        return jokeService.update(id, joke)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jokeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
