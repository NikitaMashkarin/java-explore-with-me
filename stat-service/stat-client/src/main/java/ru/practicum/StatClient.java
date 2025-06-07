package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class StatClient {

    private final RestTemplate rest;
    private final String statsServiceUri;

    public StatClient(@Value("${statistics-server.url:http://localhost:9090}") String statsServiceUri,
                      RestTemplate rest) {
        this.rest = rest;
        this.statsServiceUri = statsServiceUri;
    }

    public void addHit(HitDto hitDto) {
        String url = statsServiceUri + "/hit";
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);

        log.info("POST-запрос отправлен по {}", url);
        try {
            rest.exchange(url, HttpMethod.POST, requestEntity, Object.class);
            log.info("Выполнено");
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса на {}", url, e);
        }
    }

    public ResponseEntity<StatDto[]> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique);

        StringBuilder pathBuilder = new StringBuilder(statsServiceUri)
                .append("/stats?start={start}&end={end}&unique={unique}");

        if (uris != null && uris.length > 0) {
            parameters.put("uris", uris);
            pathBuilder.append("&uris={uris}");
        }

        String path = pathBuilder.toString();
        log.info("Отправка GET-запроса в StatServer: path={}, params={}", path, parameters);

        try {
            ResponseEntity<StatDto[]> response = rest.getForEntity(path, StatDto[].class, parameters);
            log.info("Ответ от StatServer: status={}, body={}", response.getStatusCode(), response.getBody());

            return response;
        } catch (Exception e) {
            log.error("Ошибка при отправке GET-запроса в StatServer", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
