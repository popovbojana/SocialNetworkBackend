package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.FriendRequest;
import com.internship.socialnetwork.model.FriendRequestId;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {

    @Query("SELECT friendRequest FROM FriendRequest friendRequest " +
            "WHERE friendRequest.id.fromUser = :fromUserId AND friendRequest.id.toUser = :toUserId " +
            "OR friendRequest.id.fromUser = :toUserId AND friendRequest.id.toUser = :fromUserId")
    Optional<FriendRequest> findFriendRequestBetweenUsers(Long fromUserId, Long toUserId);

    @Query("SELECT friendRequest FROM FriendRequest friendRequest " +
            "WHERE friendRequest.id.fromUser = :id OR friendRequest.id.toUser = :id ")
    List<FriendRequest> findAllForUser(Long id);

    @Query("SELECT friendRequest FROM FriendRequest friendRequest " +
            "WHERE (friendRequest.id.fromUser = :id OR friendRequest.id.toUser = :id) " +
            "AND friendRequest.status = :status")
    List<FriendRequest> findAllByStatusForUser(Long id, FriendRequestStatus status);

}
