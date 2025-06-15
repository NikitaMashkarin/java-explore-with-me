package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentState;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndEventId(Long commentId, Event eventId);

    List<Comment> findByEventIdAndState(Event eventId, CommentState state);
}
