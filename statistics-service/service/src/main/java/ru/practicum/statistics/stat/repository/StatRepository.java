package ru.practicum.statistics.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistics.stat.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    List<Stat> findDistinctByUriInAndCreatedIsAfterAndCreatedIsBefore(List<String> uriList, LocalDateTime start, LocalDateTime end);

    List<Stat> findDistinctByCreatedIsAfterAndCreatedIsBefore(LocalDateTime start, LocalDateTime end);

    List<Stat> findAllByUriInAndCreatedIsAfterAndCreatedIsBefore(List<String> uriList, LocalDateTime start, LocalDateTime end);

    List<Stat> findAllByCreatedIsAfterAndCreatedIsBefore(LocalDateTime start, LocalDateTime end);
}
