package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByPostedById(Long id);

    @Query("SELECT post FROM Post post " +
            "JOIN post.postedBy user " +
            "JOIN FriendRequest friendRequest ON (user.id = friendRequest.id.fromUser OR user.id = friendRequest.id.toUser) " +
            "WHERE (friendRequest.id.fromUser = :id OR friendRequest.id.toUser = :id) " +
            "AND friendRequest.status = 'ACCEPTED' " +
            "AND user.id != :id")
    List<Post> findFriendsPostsByUserId(Long id);

}
