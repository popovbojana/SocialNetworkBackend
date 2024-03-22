package com.internship.socialnetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String phoneNumber;

    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedFriendRequests;

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL)
    private List<Comment> postedComments;

}
