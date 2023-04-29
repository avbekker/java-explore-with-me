package ru.practicum.main_service.collaboration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.main_service.enums.Status;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    public Map<Long, Integer> getRequestsByEvents(Set<Event> events) {
        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());
        return requestsRepository.findRequestsByEvent(eventsId, Status.CONFIRMED);
    }

    public Map<String, Long> getViewsByEvents(List<Event> events) {


        String start = LocalDateTime.now().minusYears(2).format(FORMATTER);


        String end = LocalDateTime.now().format(FORMATTER);
        List<String> eventUris = events.stream().map(e -> String.format("/events/%s", e.getId())).collect(Collectors.toList());
        List<ViewStatDto> viewStatDto = statisticsClient.getViews(start, end, eventUris);
        Map<String, Long> result = new HashMap<>();
        for (ViewStatDto viewStat : viewStatDto) {
            result.put(viewStat.getUri(), viewStat.getHits());
        }
        return result;
    }

    public void createHit(HitDto hitDto) {
        statisticsClient.create(hitDto);
    }
}
