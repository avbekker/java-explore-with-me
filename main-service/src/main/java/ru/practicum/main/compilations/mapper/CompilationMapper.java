package ru.practicum.main.compilations.mapper;

import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.model.Event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .id(null)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilations, Map<Long, List<EventShortDto>> allEvents) {
        return compilations.stream()
                .map(compilation -> toCompilationDto(compilation, allEvents.get(compilation.getId())))
                .collect(Collectors.toList());
    }

    private CompilationMapper() {
    }
}
