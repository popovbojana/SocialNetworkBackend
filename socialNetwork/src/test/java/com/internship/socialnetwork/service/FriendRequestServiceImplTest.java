package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.FriendRequest;
import com.internship.socialnetwork.model.FriendRequestId;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import com.internship.socialnetwork.repository.FriendRequestRepository;
import com.internship.socialnetwork.service.impl.FriendRequestServiceImpl;
import com.internship.socialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.internship.socialnetwork.dto.FriendRequestDTO.toFriendRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class FriendRequestServiceImplTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;

    private static final Long USER_ID = 1L;

    private static final Long OTHER_USER_ID = 2L;

    private static final String FRIEND_REQUEST_NOT_FOUND_MESSAGE = "Friend request between users 1 and 2 doesn't exist!";

    @Test
    void shouldCreateRequest() {
        // given
        User user = createUser(USER_ID);
        User otherUser = createUser(OTHER_USER_ID);
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), user, otherUser, FriendRequestStatus.PENDING);
        FriendRequestDTO friendRequestDTO = toFriendRequestDTO(friendRequest);

        when(userService.findById(USER_ID)).thenReturn(user);
        when(userService.findById(OTHER_USER_ID)).thenReturn(otherUser);
        when(friendRequestRepository.findById(any())).thenReturn(Optional.empty());
        when(friendRequestRepository.save(any())).thenReturn(friendRequest);

        // when
        FriendRequestDTO createdRequest = friendRequestService.create(USER_ID, OTHER_USER_ID);

        // then
        assertEquals(friendRequestDTO, createdRequest);

        // and
        verify(userService, atLeastOnce()).findById(any());
        verify(friendRequestRepository).findById(any());
        verify(friendRequestRepository).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenCreateRequest_ifRequestAlreadyExists() {
        // given
        User user = createUser(USER_ID);
        User otherUser = createUser(OTHER_USER_ID);
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);

        when(userService.findById(USER_ID)).thenReturn(user);
        when(userService.findById(OTHER_USER_ID)).thenReturn(otherUser);
        when(friendRequestRepository.findById(any())).thenReturn(Optional.of(friendRequest));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            friendRequestService.create(USER_ID, OTHER_USER_ID);
        });

        // then
        assertEquals("Friend request between users 1 and 2 already exists!", exception.getMessage());

        // and
        verify(userService, atLeastOnce()).findById(any());
        verify(friendRequestRepository).findById(any());
        verify(friendRequestRepository, never()).save(any());
    }

    @Test
    void shouldGetRequest() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);
        FriendRequestDTO friendRequestDTO = toFriendRequestDTO(friendRequest);

        when(friendRequestRepository.findFriendRequestBetweenUsers(any(), any())).thenReturn(Optional.of(friendRequest));

        // when
        FriendRequestDTO foundRequest = friendRequestService.get(USER_ID, OTHER_USER_ID);

        // then
        assertEquals(friendRequestDTO, foundRequest);

        // and
        verify(friendRequestRepository).findFriendRequestBetweenUsers(any(), any());
    }

    @Test
    void shouldThrowNotFoundException_whenGetRequest_ifRequestDoesntExist() {
        // given
        when(friendRequestRepository.findFriendRequestBetweenUsers(any(), any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            friendRequestService.get(USER_ID, OTHER_USER_ID);
        });

        // then
        assertEquals(FRIEND_REQUEST_NOT_FOUND_MESSAGE, exception.getMessage());

        // and
        verify(friendRequestRepository).findFriendRequestBetweenUsers(any(), any());
    }

    @Test
    void shouldGetAllRequestsForUser() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);
        FriendRequestDTO friendRequestDTO = toFriendRequestDTO(friendRequest);

        when(friendRequestRepository.findAllForUser(any())).thenReturn(List.of(friendRequest));

        // when
        List<FriendRequestDTO> foundRequests = friendRequestService.getAllForUser(USER_ID);

        // then
        assertEquals(List.of(friendRequestDTO), foundRequests);

        // and
        verify(friendRequestRepository).findAllForUser(any());
    }

    @Test
    void shouldGetAllRequestsByStatusForUser() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);
        FriendRequestDTO friendRequestDTO = toFriendRequestDTO(friendRequest);

        when(friendRequestRepository.findAllByStatusForUser(any(), any())).thenReturn(List.of(friendRequest));

        // when
        List<FriendRequestDTO> foundRequests = friendRequestService.getAllByStatusForUser(USER_ID, FriendRequestStatus.PENDING);

        // then
        assertEquals(List.of(friendRequestDTO), foundRequests);

        // and
        verify(friendRequestRepository).findAllByStatusForUser(any(), any());
    }

    @Test
    void shouldRespondToPendingRequest() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);
        FriendRequest acceptedFriendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.ACCEPTED);
        FriendRequestDTO friendRequestDTO = toFriendRequestDTO(acceptedFriendRequest);

        when(friendRequestRepository.findById(any())).thenReturn(Optional.of(friendRequest));
        when(friendRequestRepository.save(any())).thenReturn(acceptedFriendRequest);

        // when
        FriendRequestDTO foundRequests = friendRequestService.respondToPendingRequest(USER_ID, OTHER_USER_ID, FriendRequestStatus.ACCEPTED);

        // then
        assertEquals(friendRequestDTO, foundRequests);

        // and
        verify(friendRequestRepository).findById(any());
        verify(friendRequestRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenRespondToPendingRequest_ifRequestDoesntExist() {
        // given
        when(friendRequestRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            friendRequestService.respondToPendingRequest(USER_ID, OTHER_USER_ID, FriendRequestStatus.ACCEPTED);
        });

        // then
        assertEquals(FRIEND_REQUEST_NOT_FOUND_MESSAGE, exception.getMessage());

        // and
        verify(friendRequestRepository).findById(any());
        verify(friendRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenRespondToPendingRequest_ifRequestIsntPending() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.ACCEPTED);

        when(friendRequestRepository.findById(any())).thenReturn(Optional.of(friendRequest));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            friendRequestService.respondToPendingRequest(USER_ID, OTHER_USER_ID, FriendRequestStatus.ACCEPTED);
        });

        // then
        assertEquals("Friend request isn't pending!", exception.getMessage());

        // and
        verify(friendRequestRepository).findById(any());
        verify(friendRequestRepository, never()).save(any());
    }

    @Test
    void shouldDeleteRequest() {
        // given
        FriendRequest friendRequest = createFriendRequest(createFriendRequestId(USER_ID, OTHER_USER_ID), createUser(USER_ID), createUser(OTHER_USER_ID), FriendRequestStatus.PENDING);

        when(friendRequestRepository.findFriendRequestBetweenUsers(any(), any())).thenReturn(Optional.of(friendRequest));

        // when
        friendRequestService.delete(USER_ID, OTHER_USER_ID);

        // then

        // and
        verify(friendRequestRepository).findFriendRequestBetweenUsers(any(), any());
        verify(friendRequestRepository).delete(any());
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteRequest_ifRequestDoesntExist() {
        // given
        when(friendRequestRepository.findFriendRequestBetweenUsers(any(), any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            friendRequestService.delete(USER_ID, OTHER_USER_ID);
        });

        // then
        assertEquals(FRIEND_REQUEST_NOT_FOUND_MESSAGE, exception.getMessage());

        // and
        verify(friendRequestRepository).findFriendRequestBetweenUsers(any(), any());
        verify(friendRequestRepository, never()).delete(any());
    }

    private FriendRequest createFriendRequest(FriendRequestId friendRequestId, User user, User otherUser, FriendRequestStatus status) {
        return FriendRequest.builder()
                .id(friendRequestId)
                .fromUser(user)
                .toUser(otherUser)
                .status(status)
                .build();
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    private FriendRequestId createFriendRequestId(Long userId, Long otherUserId) {
        return new FriendRequestId(userId, otherUserId);
    }

}
