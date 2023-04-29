package ru.practicum.main_service.users.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Size(max = 50)
    private String name;
    @Size(max = 50)
    private String email;
}
