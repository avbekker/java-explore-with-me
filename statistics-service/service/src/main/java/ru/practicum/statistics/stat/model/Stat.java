package ru.practicum.statistics.stat.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private long id;

    @Column(name = "APP", nullable = false, length = 100)
    private String app;

    @Column(name = "URI", nullable = false, length = 100)
    private String uri;

    @Column(name = "IP", nullable = false, length = 100)
    private String ip;

    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Stat that = (Stat) obj;
        return Objects.equals(id, that.id);
    }
}