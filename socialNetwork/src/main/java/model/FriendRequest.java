package model;

import jakarta.persistence.*;
import lombok.*;
import model.enumeration.FriendRequestStatus;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "friendRequests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;


    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

}
