package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.user.model.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@AuthenticationPrincipal CustomUserDetails user,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(user.getId(), newEventDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@AuthenticationPrincipal CustomUserDetails user,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getEvents(user.getId(), from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEventById(@AuthenticationPrincipal CustomUserDetails user,
                                         @PathVariable @Positive Long eventId) {
        return eventService.getEventById(user.getId(), eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateUserEvent(@AuthenticationPrincipal CustomUserDetails user,
                                        @PathVariable @Positive Long eventId,
                                        @RequestBody @Valid UpdateEventUserRequestDto updateEventUserRequestDto) {
        return eventService.updateEventByUser(user.getId(), eventId, updateEventUserRequestDto);
    }

    @GetMapping("/admin/events")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventFullDto> getAdminEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public EventFullDto updateAdminEvent(@PathVariable @Positive Long eventId,
                                         @RequestBody @Valid UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequestDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getPublishedEvents(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getPublishedEventById(@PathVariable @Positive Long id,
                                              HttpServletRequest request) {
        return eventService.getPublishedEventById(id, request);
    }
}