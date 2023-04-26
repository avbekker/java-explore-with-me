package ru.practicum.main_service.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.locations.model.Location;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "CREATED_ON")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;

    @Embedded
    @JoinColumns({@JoinColumn(name = "LOT"), @JoinColumn(name = "LON")})
    private Location location;

    private Boolean paid;

    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit;

    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;

    @Column(name = "REQUEST_MODERATION")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    private String title;

    @ManyToMany
    @ToString.Exclude
    private List<Compilation> compilations;

    @Column(name = "NOT_AVAILABLE")
    private boolean isNotAvailable;
}
