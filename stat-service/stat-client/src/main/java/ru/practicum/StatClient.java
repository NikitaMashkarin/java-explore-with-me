package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StatClient {
    private final RestTemplate rest;

    public StatClient() {
        this.rest = new RestTemplate();
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        log.info("POST-запрос отправлен по http://localhost:9090/hit");
        try {
            rest.exchange("http://localhost:9090/hit", HttpMethod.POST, requestEntity, Object.class);
            log.info("Выполнено");
        } catch (Exception e) {
            log.error("Ошибка: ", e);
        }
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique);

        StringBuilder pathBuilder = new StringBuilder("http://localhost:9090/stats/?start={start}&end={end}&unique={unique}");

        if (uris != null && uris.length > 0) {
            parameters.put("uris", uris);
            pathBuilder.append("&uris={uris}");
        }

        String path = pathBuilder.toString();
        log.info("GET-запрос отправлен по {}", path);

        try {
            ResponseEntity<Object> response = rest.getForEntity(path, Object.class, parameters);
            log.info("Ответ: status={}, body={}", response.getStatusCode(), response.getBody());
            return response;
        } catch (Exception e) {
            log.error("Ошибка", e);
            return ResponseEntity.status(500).body("Ошибка при обращении к серверу");
        }
    }
}