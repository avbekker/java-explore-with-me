package ru.practicum.client.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsClient extends BaseClient {

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

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uriList, Boolean unique) {
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
}
