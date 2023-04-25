package ru.practicum.main_service.locations.dto;

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
