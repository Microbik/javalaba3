package com.example.lab1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "joke_call")
@Data
public class JokeCall {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "joke_call_seq")
    @SequenceGenerator(name = "joke_call_seq", sequenceName = "joke_call_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joke_id", nullable = false)
    private Joke joke;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "call_time", nullable = false)
    private LocalDateTime callTime;

    @PrePersist
    protected void onCreate() {
        callTime = LocalDateTime.now();
    }
}
