package ru.practicum.main.locations.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private float lat;
    private float lon;
}
