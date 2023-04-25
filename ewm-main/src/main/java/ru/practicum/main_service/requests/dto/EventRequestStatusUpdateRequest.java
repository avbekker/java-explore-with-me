package ru.practicum.main_service.requests.dto;

import lombok.*;
import ru.practicum.main_service.enums.Status;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}