package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&()-_+=]{8,}$")
    private String password;

    private String phoneNumber;

    public static User mapToUser(NewUserDTO newUserDTO){
        return User.builder()
                .email(newUserDTO.getEmail())
                .firstName(newUserDTO.getFirstName())
                .lastName(newUserDTO.getLastName())
                .username(newUserDTO.getUsername())
                //TODO: encode passwords
                .password(newUserDTO.getPassword())
                .phoneNumber(newUserDTO.getPhoneNumber())
                .build();
    }

}
