package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;

import java.util.List;

public interface FriendRequestService {

    FriendRequestDTO create(Long fromUserId, Long toUserId);

    FriendRequestDTO get(Long fromUserId, Long toUserId);

    List<FriendRequestDTO> getAllForUser(Long id);

    List<FriendRequestDTO> getAllByStatusForUser(Long id, FriendRequestStatus friendRequestDTO);

    FriendRequestDTO respondToPendingRequest(Long fromUserId, Long toUserId, FriendRequestStatus friendRequestDTO);

    void delete(Long fromUserId, Long toUserId);
}
