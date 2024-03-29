package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.FriendRequest;
import com.internship.socialnetwork.model.FriendRequestId;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import com.internship.socialnetwork.repository.FriendRequestRepository;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.FriendRequestService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.FriendRequestDTO.toFriendRequestDTO;

@RequiredArgsConstructor
@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private static final String FRIEND_REQUEST_NOT_FOUND_MESSAGE = "Friend request between users %s and %s doesn't exist!";

    private final FriendRequestRepository friendRequestRepository;

    private final UserService userService;

    @Override
    public FriendRequestDTO create(Long fromUserId, Long toUserId) {
        FriendRequestId friendRequestId = new FriendRequestId(fromUserId, toUserId);
        User fromUser = userService.findById(friendRequestId.getFromUser());
        checkIfLimitIsReached(fromUserId);
        User toUser = userService.findById(friendRequestId.getToUser());
        friendRequestRepository.findById(friendRequestId)
                .ifPresent(friendRequest -> {
                    throw new BadRequestException(String.format("Friend request between users %s and %s already exists!", friendRequest.getId().getFromUser(), friendRequest.getId().getToUser()));
                });
        return toFriendRequestDTO(friendRequestRepository.save(buildFriendRequest(friendRequestId, fromUser, toUser)));
    }

    @Override
    public FriendRequestDTO get(Long fromUserId, Long toUserId) {
        return toFriendRequestDTO(findFriendRequestBetweenUsers(fromUserId, toUserId));
    }

    @Override
    public List<FriendRequestDTO> getAllForUser(Long id) {
        return friendRequestRepository.findAllForUser(id)
                .stream()
                .map(FriendRequestDTO::toFriendRequestDTO)
                .toList();
    }

    @Override
    public List<FriendRequestDTO> getAllByStatusForUser(Long id, FriendRequestStatus status) {
        return friendRequestRepository.findAllByStatusForUser(id, status)
                .stream()
                .map(FriendRequestDTO::toFriendRequestDTO)
                .toList();
    }

    @Override
    public FriendRequestDTO respondToPendingRequest(Long fromUserId, Long toUserId, FriendRequestStatus status) {
        FriendRequest friendRequest = friendRequestRepository.findById(new FriendRequestId(fromUserId, toUserId)).orElseThrow(() -> new NotFoundException(String.format(FRIEND_REQUEST_NOT_FOUND_MESSAGE, fromUserId, toUserId)));
        // TODO: check if user is to user from friend request
        if (friendRequest.getStatus() == FriendRequestStatus.PENDING) {
            if (status.equals(FriendRequestStatus.ACCEPTED)) {
                checkIfLimitIsReached(toUserId);
            }
            friendRequest.setStatus(status);
        } else {
            throw new BadRequestException("Friend request isn't pending!");
        }
        return toFriendRequestDTO(friendRequestRepository.save(friendRequest));
    }

    @Override
    public void delete(Long fromUserId, Long toUserId) {
        friendRequestRepository.delete(findFriendRequestBetweenUsers(fromUserId, toUserId));
    }

    private void checkIfLimitIsReached(Long userId) {
        // TODO: change to larger number (2 is for testing purposes)
        if (userService.getAllFriendsById(userId).size() >= 2) {
            throw new BadRequestException("Maximum number of friends limit reached.");
        }
    }

    private FriendRequest findFriendRequestBetweenUsers(Long fromUserId, Long toUserId) {
        return friendRequestRepository.findFriendRequestBetweenUsers(fromUserId, toUserId).orElseThrow(() -> new NotFoundException(String.format(FRIEND_REQUEST_NOT_FOUND_MESSAGE, fromUserId, toUserId)));
    }

    private FriendRequest buildFriendRequest(FriendRequestId friendRequestId, User fromUser, User toUser) {
        return FriendRequest.builder()
                .id(friendRequestId)
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
    }

}
