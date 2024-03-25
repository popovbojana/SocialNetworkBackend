package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void addNewUser_Success(){
        // given
        NewUserDTO newUserDTO = NewUserDTO.builder()
                .email("Test email")
                .firstName("Test first name")
                .lastName("Test last name")
                .username("Test username")
                .password("Test password")
                .phoneNumber("Test phone number")
                .build();

        User user = new User();
        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(newUserDTO, user);
        BeanUtils.copyProperties(newUserDTO, userDTO);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        UserDTO savedUser = userService.addNewUser(newUserDTO);

        // then
        assertEquals(userDTO, savedUser);

        // and
        verify(userRepository).save(any());
        verify(userRepository).findByEmail(any());
        verify(userRepository).findByUsername(any());
    }

    @Test
    public void addNewUser_UserExistsWithEmail(){
        // given
        String email = "test@email.com";

        User existingUser = User.builder()
                .email(email)
                .build();

        NewUserDTO newUserDTO = NewUserDTO.builder()
                .email(email)
                .build();

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // then
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.addNewUser(newUserDTO);
        });

        assertEquals("User with the email test@email.com already exists!", exception.getMessage());

        // and
        verify(userRepository).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void addNewUser_UserExistsWithUsername(){
        // given
        String username = "test_username";

        User existingUser = User.builder()
                .username(username)
                .build();

        NewUserDTO newUserDTO = NewUserDTO.builder()
                .username(username)
                .build();

        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        // then
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.addNewUser(newUserDTO);
        });

        assertEquals("User with the username test_username already exists!", exception.getMessage());

        // and
        verify(userRepository).findByUsername(any());
        verify(userRepository, never()).save(any());
    }

}
