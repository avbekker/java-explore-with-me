package ru.practicum.statistics.stat.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.statistics.stat.model.Stat;

public class StatMapper {

    public static Stat toStat(HitDto hitDto) {
        return Stat.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .created(hitDto.getCreated())
                .build();
    }

    public static StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }

    private StatMapper() {
    }
}