package ru.practicum.main_service.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REQUESTS")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;

    @Enumerated(EnumType.STRING)
    @Size(max = 50)
    private Status status;
}