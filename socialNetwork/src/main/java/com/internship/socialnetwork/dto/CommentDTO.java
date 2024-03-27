package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long postId;

    private Long userId;

    private String comment;

    private LocalDateTime commentedAt;

    private List<CommentDTO> replies;

    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .postId(comment.getPost().getId())
                .userId(comment.getCommentedBy().getId())
                .comment(comment.getComment())
                .commentedAt(comment.getCommentedAt())
                .replies(Optional.ofNullable(comment.getReplies())
                        .map(replies -> replies.stream().map(CommentDTO::toCommentDTO).toList())
                        .orElse(null))
                .build();
    }

}
