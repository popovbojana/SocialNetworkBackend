package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
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
public class PostDTO {

    private Long userId;

    private String description;

    private String file;

    private LocalDateTime postedAt;

    private List<CommentDTO> comments;

    public static PostDTO toPostDTO(Post post) {
        return PostDTO.builder()
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .file(Optional.ofNullable(post.getFile())
                        .map(FileData::getFilePath)
                        .orElse(null))
                .postedAt(post.getPostedAt())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

    public static PostDTO toPostDTOWithFile(Post post, FileData file) {
        return PostDTO.builder()
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .file(file.getFilePath())
                .postedAt(post.getPostedAt())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

}
