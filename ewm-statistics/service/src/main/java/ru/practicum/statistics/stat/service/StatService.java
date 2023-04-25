package ru.practicum.statistics.stat.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void create(HitDto hitDto);

    List<ViewStatDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
