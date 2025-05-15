package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.service.StatService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody HitDto hitDto) {
        log.info("POST /hit: {} begun", hitDto);
        statService.addHit(hitDto);
        log.info("POST /hit: {} completed", hitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatDto> getStatistics(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats begun");
        return statService.getStatistics(start, end, uris, unique);
    }
}
