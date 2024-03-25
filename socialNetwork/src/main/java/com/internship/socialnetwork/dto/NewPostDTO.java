package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewPostDTO {

    @NotBlank
    private String description;

    @NotBlank
    private String media;

    public static Post toPost(User user, NewPostDTO newPostDTO) {
        return Post.builder()
                .postedBy(user)
                .description(newPostDTO.getDescription())
                .media(newPostDTO.getMedia())
                .postedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();
    }

}