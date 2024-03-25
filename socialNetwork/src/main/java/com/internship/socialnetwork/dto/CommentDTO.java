package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long postId;

    private Long userId;

    private LocalDateTime commentedAt;

    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .postId(comment.getPost().getId())
                .userId(comment.getCommentedBy().getId())
                .commentedAt(comment.getCommentedAt())
                .build();
    }

}
