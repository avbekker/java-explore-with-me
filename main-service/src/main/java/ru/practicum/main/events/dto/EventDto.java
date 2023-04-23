package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.enums.State;
import ru.practicum.main.locations.dto.LocationDto;
import ru.practicum.main.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private boolean paid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private String description;
    private Integer participantLimit;
    private State state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private LocationDto location;
    private boolean requestModeration;
}