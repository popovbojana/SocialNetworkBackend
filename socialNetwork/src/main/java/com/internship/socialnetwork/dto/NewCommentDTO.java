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
public class NewCommentDTO {

    // TODO: remove later
    private Long userId;

    private String comment;

    private Long parentCommentId;

}
