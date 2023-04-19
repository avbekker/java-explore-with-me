package ru.practicum.statistics.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.stat.mapper.StatMapper;
import ru.practicum.statistics.stat.model.Stat;
import ru.practicum.statistics.stat.repository.StatRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.statistics.stat.mapper.StatMapper.toStat;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository repository;

    @Transactional
    @Override
    public void create(HitDto hitDto) {
        Stat stat = toStat(hitDto);
        repository.save(stat);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StatDto> get(LocalDateTime start, LocalDateTime end, List<String> uriList, boolean unique) {
        List<Stat> result;
        if (unique) {
            if (uriList.isEmpty()) {
                result = repository.findDistinctByCreatedIsAfterAndCreatedIsBefore(start, end);
            } else {
                result = repository.findDistinctByUriInAndCreatedIsAfterAndCreatedIsBefore(uriList, start, end);
            }
        } else {
            if (uriList.isEmpty()) {
                result = repository.findAllByCreatedIsAfterAndCreatedIsBefore(start, end);
            } else {
                result = repository.findAllByUriInAndCreatedIsAfterAndCreatedIsBefore(uriList, start, end);
            }
        }
        return result.stream().map(StatMapper::toStatDto).collect(Collectors.toList());
    }
}
