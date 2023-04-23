package ru.practicum.main.locations.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LOCATIONS")
public class Location {
    @Id
    private Float lat;
    @Id
    private Float lon;
}
