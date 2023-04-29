package ru.practicum.main_service.compilations.model;

import lombok.*;
import ru.practicum.main_service.events.model.Event;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
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
    @Size(max = 400)
    private String title;

    @ManyToMany
    @JoinTable(name = "EVENT_COMPILATION",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    private List<Event> events;
}
