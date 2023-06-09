package ru.practicum.statistics.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.statistics.stat.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query(value = "SELECT new ru.practicum.dto.ViewStatDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.uri IN :uriList AND s.created BETWEEN :start AND :end " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<ViewStatDto> statisticsWithUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                           @Param("uriList") List<String> uriList);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stat s " +
            "WHERE s.uri IN :uriList AND s.created BETWEEN :start AND :end " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<ViewStatDto> statisticsWithoutUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                              @Param("uriList") List<String> uriList);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.created BETWEEN :start AND :end " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<ViewStatDto> statisticsWithUniqueWithoutUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stat s " +
            "WHERE s.created BETWEEN :start AND :end " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<ViewStatDto> statisticsWithoutUniqueWithoutUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
