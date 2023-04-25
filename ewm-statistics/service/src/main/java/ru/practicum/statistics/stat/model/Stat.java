package ru.practicum.statistics.stat.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "STAT_HIT")
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HIT_ID", nullable = false)
    private Long id;

    @Column(name = "APP", nullable = false, length = 100)
    private String app;

    @Column(name = "URI", nullable = false, length = 100)
    private String uri;

    @Column(name = "IP", nullable = false, length = 100)
    private String ip;

    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
}