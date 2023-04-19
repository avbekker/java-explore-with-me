package ru.practicum.statistics.stat.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void create(HitDto hitDto);

    List<StatDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
