package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEventIn(List<Event> events);

    Integer countByEventIdAndStatus(Long id, RequestStatus requestStatus);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Request r SET r.status = :newStatus WHERE r.event = :event AND r.status = :searchStatus")
    void updateRequestStatusByEventIdAndStatus(@Param("event") Event event,
                                               @Param("searchStatus") RequestStatus searchStatus,
                                               @Param("newStatus") RequestStatus newStatus);

    List<Request> findByRequesterId(Long userId);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);
}
