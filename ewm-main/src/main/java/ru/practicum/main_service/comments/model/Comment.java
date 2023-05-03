package ru.practicum.main_service.comments.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE")
    @Size(max = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID")
    @NotNull
    private User creator;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    @NotNull
    private Event event;

    @Column(name = "CREATED")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "UPDATED")
    @UpdateTimestamp
    private LocalDateTime lastUpdatedOn;
}
