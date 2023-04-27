package ru.practicum.main_service.events.model;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {
    private float lat;
    private float lon;
}
