package ch.hftm.dtos;

import ch.hftm.entities.Comment;

import java.time.LocalDateTime;

public record CommentDto(String comment, LocalDateTime date, String author) {
    CommentDto(Comment c) {
        this(c.comment, c.date, c.userId);
    }
}