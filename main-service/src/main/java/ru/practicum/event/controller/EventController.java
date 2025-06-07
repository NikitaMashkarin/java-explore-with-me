package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequestDto;
import ru.practicum.event.service.EventService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private EventService service;

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable @Positive Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /users/{}/events", userId);
        return service.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @Positive Long userId,
                                 @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events", userId);
        return service.addEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventsByUserIdAndEventId(@PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        return service.getEventsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @RequestBody UpdateEventUserRequestDto eventRequestDto) {
        log.info("PATCH /users/{}/events/{}", userId, eventId);
        return service.updateEvent(userId, eventId, eventRequestDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false) boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") @Positive Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("GET /events");
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable @Positive Long id, HttpServletRequest request) {
        log.info("GET /events/{}", id);
        return service.getEventById(id, request);
    }
}
