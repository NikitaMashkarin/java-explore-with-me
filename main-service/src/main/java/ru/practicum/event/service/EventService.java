package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequestDto;

import java.util.List;

@Service
public interface EventService {
    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventsByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventRequestDto);

    List<EventShortDto> getEvents(String text, List<Long> categories, boolean paid,
                                  String rangeStart, String rangeEnd, boolean onlyAvailable,
                                  String sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);
}
