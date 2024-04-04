package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDTO {

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 500, message = "can have maximum 1000 characters")
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
