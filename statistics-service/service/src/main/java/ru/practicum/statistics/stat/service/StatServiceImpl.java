package ru.practicum.statistics.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.statistics.excemption.StartEndTimeException;
import ru.practicum.statistics.stat.model.Stat;
import ru.practicum.statistics.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.statistics.stat.mapper.StatMapper.toStat;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
class StatServiceImpl implements StatService {

    private final StatRepository repository;

    @Transactional
    @Override
    public void create(HitDto hitDto) {
        Stat stat = toStat(hitDto);
        stat.setCreated(LocalDateTime.now());
        repository.save(stat);
    }

    @Override
    public List<StatDto> get(LocalDateTime start, LocalDateTime end, List<String> uriList, boolean unique) {
        if (end.isBefore(start)) {
            throw new StartEndTimeException("End time cannot be before start time.");
        }
        if (unique) {
            if (uriList == null) {
                return repository.statisticsWithUniqueWithoutUri(start, end);
            } else {
                return repository.statisticsWithUnique(start, end, uriList);
            }
        } else {
            if (uriList == null) {
                return repository.statisticsWithoutUniqueWithoutUri(start, end);
            } else {
                return repository.statisticsWithoutUnique(start, end, uriList);
            }
        }
    }
}
