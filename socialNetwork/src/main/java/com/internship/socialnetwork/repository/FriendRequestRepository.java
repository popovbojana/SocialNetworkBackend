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
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findById(FriendRequestId friendRequestId);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.id.fromUser = :userId1 AND fr.id.toUser = :userId2 " +
            "OR fr.id.fromUser = :userId2 AND fr.id.toUser = :userId1")
    Optional<FriendRequest> findFriendRequestBetweenUsers(Long userId1, Long userId2);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.id.fromUser = :id OR fr.id.toUser = :id ")
    List<FriendRequest> findAllForUser(Long id);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE (fr.id.fromUser = :id OR fr.id.toUser = :id) " +
            "AND fr.status = :status")
    List<FriendRequest> findAllByStatusForUser(Long id, FriendRequestStatus status);

}
