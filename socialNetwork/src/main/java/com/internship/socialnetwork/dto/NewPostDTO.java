package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDTO {

    @NotBlank
    @Size(max = 1000, message = "can have maximum 1000 characters")
    private String description;

    private List<MultipartFile> files;

    public static Post toPost(User user, NewPostDTO newPostDTO) {
        return Post.builder()
                .postedBy(user)
                .description(newPostDTO.getDescription())
                .postedAt(LocalDateTime.now())
                .build();
    }

}
