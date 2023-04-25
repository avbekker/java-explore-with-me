package ru.practicum.stats_client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsClient extends BaseClient {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public StatisticsClient(@Value("${statistics-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(HitDto hitDto) {
        String path = "/hit";
        return post(hitDto, path);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique) {
        String path = "/stats";
        Map<String, Object> parameters;
        if (unique == null) {
            if (uriList == null || uriList.isEmpty()) {
                parameters = Map.of(
                        "start", start,
                        "end", end);
                return get(path + "?start={start}&end={end}", parameters);
            } else {
                parameters = Map.of(
                        "start", start,
                        "end", end,
                        "uris", String.join(",", uriList));
                return get(path + "?start={start}&end={end}&uris={uris}", parameters);
            }
        } else {
            if (uriList == null || uriList.isEmpty()) {
                parameters = Map.of(
                        "start", start,
                        "end", end,
                        "unique", unique);
                return get(path + "?start={start}&end={end}&unique={unique}", parameters);
            } else {
                parameters = Map.of(
                        "start", start,
                        "end", end,
                        "uris", String.join(",", uriList),
                        "unique", unique);
                return get(path + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
            }
        }
    }

    public Map<Long, Long> getViews(List<Long> eventsIds, LocalDateTime start) {
        if (start == null) {
            return Collections.emptyMap();
        }
        List<String> uris = eventsIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());
        ResponseEntity<Object> statsDto = getStats(start, LocalDateTime.now(), uris, false);
        if (statsDto == null) {
            return Collections.emptyMap();
        }
        List<ViewStatDto> result = objectMapper.convertValue(statsDto, new TypeReference<>() {
        });
        if (result.isEmpty()) {
            return Collections.emptyMap();
        }
        return result.stream().collect(Collectors.toMap(statDto -> Long.parseLong(statDto.getUri().substring(8)),
                ViewStatDto::getHits));
    }
}
