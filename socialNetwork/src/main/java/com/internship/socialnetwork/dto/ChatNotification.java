package com.internship.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {

    private Long id;

    private Long senderId;

    private Long recipientId;

    private String content;

}
