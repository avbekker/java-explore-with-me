package ru.practicum.main_service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 400)
    private String title;
}

