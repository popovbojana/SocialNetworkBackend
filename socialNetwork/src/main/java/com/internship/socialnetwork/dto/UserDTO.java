package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String phoneNumber;

    private int postsCount;

    private int friendsCount;

    public static UserDTO toUserDTO(User user, int postsCount, int friendsCount) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .postsCount(postsCount)
                .friendsCount(friendsCount)
                .build();
    }

}