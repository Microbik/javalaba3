package com.example.lab1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;


@Entity
@Table(name = "joke")
@Data
public class Joke {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "joke_seq")
    @SequenceGenerator(name = "joke_seq", sequenceName = "joke_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JokeCall> calls = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}