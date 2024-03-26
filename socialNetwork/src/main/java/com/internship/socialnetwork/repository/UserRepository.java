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

    @Query("SELECT u FROM User u " +
            "JOIN FriendRequest fr ON (u.id = fr.id.fromUser OR u.id = fr.id.toUser) " +
            "WHERE (fr.id.fromUser = :id OR fr.id.toUser = :id) " +
            "AND fr.status = 'ACCEPTED' " +
            "AND u.id != :id")
    List<User> findFriendsById(Long id);

}
