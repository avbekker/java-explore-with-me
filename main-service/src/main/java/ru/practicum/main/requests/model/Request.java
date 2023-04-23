package ru.practicum.main.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.enums.Status;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REQUESTS")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;

    @Enumerated(EnumType.STRING)
    private Status status;
}