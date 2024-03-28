package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT user FROM User user " +
            "JOIN FriendRequest friendRequest ON (user.id = friendRequest.id.fromUser OR user.id = friendRequest.id.toUser) " +
            "WHERE (friendRequest.id.fromUser = :id OR friendRequest.id.toUser = :id) " +
            "AND friendRequest.status = 'ACCEPTED' " +
            "AND user.id != :id")
    List<User> findFriendsById(Long id);

}
