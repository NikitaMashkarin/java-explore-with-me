package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.StatClient;
import ru.practicum.StatDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequestDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateUserAction;
import ru.practicum.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.practicum.exceptions.CategoryNotFoundException;
import ru.practicum.exceptions.EventNotFoundException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.exceptions.ValidationRequestException;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import static ru.practicum.event.mapper.EventMapper.toEvent;
import static ru.practicum.event.mapper.EventMapper.toEventFullDto;
import static ru.practicum.location.mapper.LocationMapper.toLocation;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private EventRepository eventRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private final StatClient statClient;

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable).getContent();

        return events.isEmpty()
                ? List.of()
                : events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory()));

        if (newEventDto.getParticipantLimit() < 0) {
            throw new ValidationRequestException("Лимит участников не может быть отрицательным");
        }

        Event event = toEvent(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException("Событие должно начинаться минимум через 2 часа");
        }

        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setLocation(locationRepository.save(toLocation(newEventDto.getLocation())));
        event.setViews(0L);

        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventsByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Optional<Event> event = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (event.isEmpty()) throw new EventNotFoundException(eventId);

        return toEventFullDto(event.get());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Optional<Event> eventOld = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (eventOld.isEmpty()) throw new EventNotFoundException(eventId);

        Event eventUpdate = eventOld.get();

        if (eventUpdate.getState() == EventState.PUBLISHED)
            throw new ValidationRequestException("Only pending or canceled events can be changed");

        if (eventRequestDto.getAnnotation() != null) eventUpdate.setAnnotation(eventRequestDto.getAnnotation());

        if (eventRequestDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventRequestDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(eventRequestDto.getCategory()));

            eventUpdate.setCategory(category);
        }

        if (eventRequestDto.getDescription() != null) eventUpdate.setDescription(eventUpdate.getDescription());

        if (eventRequestDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventRequestDto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationRequestException("Дата события должна быть минимум через 2 часа");
            }
            eventUpdate.setEventDate(eventDate);
        }

        if (eventRequestDto.getLocation() != null) eventUpdate.setLocation(toLocation(eventRequestDto.getLocation()));

        if (eventRequestDto.getPaid() != null) eventUpdate.setPaid(eventRequestDto.getPaid());

        if (eventRequestDto.getParticipantLimit() != null) {
            if (eventRequestDto.getParticipantLimit() < 0)
                throw new ValidationRequestException("Лимит участников не может быть отрицательным");

            eventUpdate.setParticipantLimit(eventRequestDto.getParticipantLimit());
        }

        if (eventRequestDto.getRequestModeration() != null)
            eventUpdate.setRequestModeration(eventRequestDto.getRequestModeration());

        if (eventRequestDto.getStateAction() == StateUserAction.SEND_TO_REVIEW) {
            eventUpdate.setState(EventState.PENDING);
        } else if (eventRequestDto.getStateAction() == StateUserAction.CANCEL_REVIEW) {
            eventUpdate.setState(EventState.CANCELED);
        }

        if (eventRequestDto.getTitle() != null) eventUpdate.setTitle(eventRequestDto.getTitle());

        return toEventFullDto(eventRepository.save(eventUpdate));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, boolean paid,
                                         String rangeStart, String rangeEnd, boolean onlyAvailable,
                                         String sort, Integer from, Integer size, HttpServletRequest request) {
        sendHit(request);

        LocalDateTime start = null;
        LocalDateTime end = null;

        try {
            if (rangeStart != null) {
                start = LocalDateTime.parse(rangeStart, formatter);
            }
            if (rangeEnd != null) {
                end = LocalDateTime.parse(rangeEnd, formatter);
            }
        } catch (Exception e) {
            throw new ValidationRequestException("Неверный формат даты. Ожидается: yyyy-MM-dd HH:mm:ss");
        }

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationRequestException("Дата начала должна быть раньше даты окончания.");
        }

        List<Event> events = eventRepository.findPublishedEvents(
                text,
                categories,
                paid,
                start != null ? start : LocalDateTime.now(),
                end,
                PageRequest.of(from / size, size)
        );

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> EventMapper.countConfirmedRequests(event.getRequests()) < event.getParticipantLimit())
                    .toList();
        }

        List<EventShortDto> dtos = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                dtos.sort(Comparator.comparing(EventShortDto::getEventDate));
            } else if (sort.equals("VIEWS")) {
                dtos.sort(Comparator.comparing(EventShortDto::getViews));
            } else {
                throw new ValidationRequestException("Неверный параметр сортировки.");
            }
        }

        return dtos;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty()) throw new EventNotFoundException(id);

        Event event = eventOptional.get();

        int previousHits = getHits(request);
        sendHit(request);
        int newHits = getHits(request);

        if (newHits > previousHits) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        return toEventFullDto(event);
    }

    private void sendHit(HttpServletRequest request) {
        statClient.addHit(HitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
    }

    private int getHits(HttpServletRequest request) {
        ResponseEntity<StatDto[]> response = statClient.getStats(
                LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().format(formatter),
                new String[]{request.getRequestURI()},
                true
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            return Math.toIntExact(response.getBody()[0].getHits());
        }
        return 0;
    }
}
