package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long userId;

    private String description;

    private String media;

    private LocalDateTime postedAt;

    private List<CommentDTO> comments;

    public static PostDTO toPostDTO(Post post) {
        return PostDTO.builder()
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .media(post.getMedia())
                .postedAt(post.getPostedAt())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

}
