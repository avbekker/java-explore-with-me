package ru.practicum.main_service.events.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
