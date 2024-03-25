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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&()-_+=]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    private String phoneNumber;

    public static User toUser(NewUserDTO newUserDTO){
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
