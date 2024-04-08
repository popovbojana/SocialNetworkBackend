package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long userId;

    private String description;

    private List<String> files;

    private LocalDateTime postedAt;

    private List<CommentDTO> comments;

    public static PostDTO toPostDTO(Post post) {
        return PostDTO.builder()
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .files(post.getFiles()
                        .stream()
                        .map(FileData::getName)
                        .toList())
                .postedAt(post.getPostedAt())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

    public static PostDTO toPostDTOWithFiles(Post post, List<FileData> files) {
        return PostDTO.builder()
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .files(files.stream()
                        .map(FileData::getName)
                        .toList())
                .postedAt(post.getPostedAt())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

}
