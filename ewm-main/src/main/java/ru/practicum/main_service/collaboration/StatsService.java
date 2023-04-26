package ru.practicum.main_service.collaboration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.requests.repository.RequestsRepository;
import ru.practicum.stats_client.StatisticsClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public Map<Long, Long> getViewsByEvents(List<Event> events) {
        LocalDateTime start = events.stream().map(Event::getPublishedOn).filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        return statisticsClient.getViews(eventIds, start);
    }

    public void createHit(HitDto hitDto) {
        statisticsClient.create(hitDto);
    }
}
