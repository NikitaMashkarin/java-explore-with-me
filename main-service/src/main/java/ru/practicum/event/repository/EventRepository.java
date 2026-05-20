package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    List<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findByIdIn(List<Long> eventIds);

    Event findFirstByCategoryId(Long catId);

    @Query("""
       SELECT e FROM Event e
       WHERE (:users IS NULL OR e.initiator.id IN :users)
       AND (:states IS NULL OR e.state IN :states)
       AND (:categories IS NULL OR e.category.id IN :categories)
       AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart)
       AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
       """)
    List<Event> findEvents(
            @Param("users") List<Long> users,
            @Param("states") List<String> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("""
    SELECT e
    FROM Event e
    WHERE e.state = ru.practicum.event.model.EventState.PUBLISHED
      AND (
            :text IS NULL
            OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))
          )
      AND (
            :categories IS NULL
            OR e.category.id IN :categories
          )
      AND (
            :paid IS NULL
            OR e.paid = :paid
          )
      AND e.eventDate >= :rangeStart
      AND (
            :rangeEnd IS NULL
            OR e.eventDate < :rangeEnd
          )
""")
    List<Event> findPublishedEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    Optional<Event> findByIdAndState(Long eventId, EventState state);
}