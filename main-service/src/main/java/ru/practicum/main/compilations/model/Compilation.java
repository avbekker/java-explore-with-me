package ru.practicum.main.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPILATIONS")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    private Boolean pinned;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "EVENT_COMPILATION",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    private List<Event> events;
}
