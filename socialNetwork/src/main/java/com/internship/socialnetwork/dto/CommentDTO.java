package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private Long postId;

    private Long userId;

    private String comment;

    private String commentedAt;

    private List<CommentDTO> replies;

    private Long parentCommentId;

    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getCommentedBy().getId())
                .comment(comment.getComment())
                .commentedAt(comment.getCommentedAt().toString())
                .replies(Optional.ofNullable(comment.getReplies())
                        .map(replies -> replies.stream().map(CommentDTO::toCommentDTO).toList())
                        .orElse(null))
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .build();
    }

}
