package ru.practicum.comments.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class CommentMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(NewCommentDto newCommentDto){
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .createdOn(comment.getCreatedOn())
                .state(comment.getState())
                .text(comment.getText())
                .updatedOn(comment.getUpdatedOn())
                .build();
    }
}
