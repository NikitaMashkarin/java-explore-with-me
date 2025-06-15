package ru.practicum.comments.service;

import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentState;
import ru.practicum.exceptions.CommentNotFoundException;
import ru.practicum.exceptions.ForbiddenException;

import java.util.List;

import static ru.practicum.comments.mapper.CommentMapper.toCommentResponseDto;

@Service
public interface CommentService {
    CommentResponseDto createComment(Long userId, Long eventId, NewCommentDto dto);

    CommentResponseDto updateComment(Long userId, Long commentId, NewCommentDto dto);

    List<CommentResponseDto> getCommentsByEventId(Long eventId);

    CommentResponseDto getCommentById(Long commentId);

    void deleteComment(Long userId, Long commentId);

    CommentResponseDto updateState(Long commentId, boolean state);
}
