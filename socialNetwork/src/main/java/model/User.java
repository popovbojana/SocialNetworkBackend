package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
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

    @ManyToMany
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

    @OneToMany(mappedBy = "fromUser")
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "toUser")
    private List<FriendRequest> receivedFriendRequests;

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL)
    private List<Comment> postedComments;

}
