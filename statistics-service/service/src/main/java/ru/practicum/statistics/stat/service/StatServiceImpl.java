package ru.practicum.statistics.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.statistics.stat.model.Stat;
import ru.practicum.statistics.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

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
        stat.setCreated(LocalDateTime.now());
        repository.save(stat);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StatDto> get(LocalDateTime start, LocalDateTime end, List<String> uriList, boolean unique) {
        List<StatDto> result;
        if (unique) {
            result = repository.statisticsWithUnique(start, end, uriList);
        } else {
            result = repository.statisticsWithoutUnique(start, end, uriList);
        }
        return result;
    }
}
