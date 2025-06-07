package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@Validated
@Slf4j
public class RequestController {
    private RequestService service;

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getRequestByUserIdAndEventId(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId) {
        log.info("GET /users/{}/events/{}/request", userId, eventId);
        return service.getRequestByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequest(@PathVariable @Positive Long userId,
                                                           @PathVariable @Positive Long eventId,
                                                           @RequestBody EventRequestStatusUpdateRequestDto updateEvent) {
        log.info("PATCH /users/{}/events/{}/request", userId, eventId);
        return service.updateRequest(userId, eventId, updateEvent);
    }

    @GetMapping("/requests")
    public List<RequestDto> getRequests(@PathVariable @Positive Long userId) {
        log.info("GET  /users/{}/requests", userId);
        return service.getRequests(userId);
    }

    @PostMapping("/requests")
    public RequestDto addRequests(@PathVariable @Positive Long userId,
                                  @RequestParam @Positive Long eventId) {
        log.info("POST /users/{}/requests", userId);
        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto deleteRequest(@PathVariable @Positive Long userId,
                                    @RequestParam @Positive Long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return service.deleteRequest(userId, requestId);
    }
}
