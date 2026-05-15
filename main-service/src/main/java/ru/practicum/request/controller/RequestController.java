package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;
import ru.practicum.user.model.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createParticipationRequest(@AuthenticationPrincipal CustomUserDetails user,
                                                 @RequestParam @Positive Long eventId) {
        return requestService.createParticipationRequest(user.getId(), eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto cancelParticipationRequest(@AuthenticationPrincipal CustomUserDetails user,
                                                 @PathVariable @Positive Long requestId) {
        return requestService.cancelParticipationRequest(user.getId(), requestId);
    }

    @GetMapping("/requests")
    public List<RequestDto> getParticipationRequests(@AuthenticationPrincipal CustomUserDetails user) {
        return requestService.getParticipationRequests(user.getId());
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getParticipationRequestsForUserEvent(@AuthenticationPrincipal CustomUserDetails user,
                                                                 @PathVariable @Positive Long eventId) {
        return requestService.getParticipationRequestsForUserEvent(user.getId(), eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeParticipationRequestsStatus(@AuthenticationPrincipal CustomUserDetails user,
                                                                               @PathVariable @Positive Long eventId,
                                                                               @RequestBody EventRequestStatusUpdateRequestDto requestDto) {
        return requestService.changeParticipationRequestsStatus(user.getId(), eventId, requestDto);
    }
}