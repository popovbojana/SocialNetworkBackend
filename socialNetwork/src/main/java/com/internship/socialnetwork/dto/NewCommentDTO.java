package com.internship.socialnetwork.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDTO {

    @NotNull
    private Long userId;

    @Size(max = 500, message = "can have maximum 500 characters")
    private String comment;

    private Long parentCommentId;

}
