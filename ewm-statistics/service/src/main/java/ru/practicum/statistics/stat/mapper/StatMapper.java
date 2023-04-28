package ru.practicum.statistics.stat.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.statistics.stat.model.Stat;

public class StatMapper {

    public static Stat toStat(HitDto hitDto) {
        return Stat.builder()
                .id(null)
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .created(hitDto.getCreated())
                .build();
    }

    public static HitDto toHitDto(Stat stat) {
        return HitDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .created(stat.getCreated())
                .build();
    }

    private StatMapper() {
    }
}