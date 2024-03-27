package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.FriendRequest;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {

    private Long fromUserId;

    private Long toUserId;

    private FriendRequestStatus status;

    public static FriendRequestDTO toFriendRequestDTO(FriendRequest friendRequest) {
        return FriendRequestDTO.builder()
                .fromUserId(friendRequest.getId().getFromUser())
                .toUserId(friendRequest.getId().getToUser())
                .status(friendRequest.getStatus())
                .build();
    }

}
