package ru.practicum.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

@Service
public interface RequestService {
    List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequestDto dto);

    List<RequestDto> getRequests(Long userId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto deleteRequest(Long userId, Long requestId);
}
