package ru.practicum.comments.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.service.CommentService;
import ru.practicum.user.model.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comment/")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@AuthenticationPrincipal CustomUserDetails user,
                                            @PathVariable @Positive Long eventId,
                                            @RequestBody NewCommentDto newCommentDto) {
        return commentService.createComment(user.getId(), eventId, newCommentDto);
    }

    @PatchMapping("/comment/{commentId}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal CustomUserDetails user,
                                            @PathVariable @Positive Long commentId,
                                            @RequestBody NewCommentDto newCommentDto) {
        return commentService.updateComment(user.getId(), commentId, newCommentDto);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentResponseDto> getCommentsByEventId(@PathVariable @Positive Long eventId) {
        return commentService.getCommentsByEventId(eventId);
    }

    @GetMapping("/comment/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable @Positive Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@AuthenticationPrincipal CustomUserDetails user,
                              @PathVariable @Positive Long commentId) {
        commentService.deleteComment(user.getId(), commentId);
    }
}
