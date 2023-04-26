package ru.practicum.main_service.collaboration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsService {
    private final RequestsRepository requestsRepository;
    private final StatisticsClient statisticsClient;

    public Map<Long, Long> getRequestsByEvents(List<Event> events) {
        Map<Long, Long> confirmedRequestsByEvents = new HashMap<>();
        long confirmationRequests;
        for (Event event : events) {
            confirmationRequests = requestsRepository.findByEvent(event).size();
            confirmedRequestsByEvents.put(event.getId(), confirmationRequests);
        }
        return confirmedRequestsByEvents;
    }

    public Map<String, Long> getViewsByEvents(List<Event> events) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(2).format(customFormatter);
        String end = LocalDateTime.now().plusYears(2).format(customFormatter);
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
