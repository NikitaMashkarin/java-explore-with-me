package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    List<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.state = 'PUBLISHED'
              AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', CAST(:text AS TEXT), '%'))
                                  OR LOWER(e.description) LIKE LOWER(CONCAT('%', CAST(:text AS TEXT), '%')) )
              AND (:categories IS NULL OR e.category_id IN (CAST(CAST(:categories AS TEXT) AS BIGINT)))
              AND (:paid IS NULL OR e.paid = CAST(CAST(:paid AS TEXT) AS BOOLEAN))
              AND (e.event_date >= :rangeStart)
              AND (CAST(:rangeEnd AS timestamp) IS NULL OR e.event_date < CAST(:rangeEnd AS timestamp))
            """,
            nativeQuery = true)
    List<Event> findPublishedEvents(@Param("text") String text,
                                    @Param("categories") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    List<Event> findByIdIn(List<Long> events);
}
