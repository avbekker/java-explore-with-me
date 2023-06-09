package ru.practicum.stats_client;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient extends BaseClient {
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

    public ResponseEntity<Object> getStats(String start, String end, List<String> uriList, Boolean unique) {
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

    public List<ViewStatDto> getViews(String start, String end, List<String> uriList) {
        Gson gson = new Gson();
        ResponseEntity<Object> viewsObject = getStats(start, end, uriList, false);
        String json = gson.toJson(viewsObject.getBody());
        ViewStatDto[] viewsArray = gson.fromJson(json, ViewStatDto[].class);
        return Arrays.asList(viewsArray);
    }
}
