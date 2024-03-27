package com.internship.socialnetwork.model;

import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    @EmbeddedId
    private FriendRequestId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;


    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

}
