package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.beans.BeanUtils.copyProperties;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final Long USER_ID = 1L;

    @Test
    void shouldSaveUser_whenCreateUser_ifUserDoesntExist() {
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

        copyProperties(newUserDTO, user);
        copyProperties(newUserDTO, userDTO);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        UserDTO savedUser = userService.create(newUserDTO);

        // then
        assertEquals(userDTO, savedUser);

        // and
        verify(userRepository).save(any());
        verify(userRepository).findByEmail(any());
        verify(userRepository).findByUsername(any());
    }

    @Test
    void shouldThrowBadRequestException_whenCreateUser_ifUserWithEmailExists() {
        // given
        String email = "test@email.com";

        User existingUser = User.builder()
                .email(email)
                .build();

        NewUserDTO newUserDTO = NewUserDTO.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // when
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.create(newUserDTO);
        });

        // then
        assertEquals("User with the email test@email.com already exists!", exception.getMessage());

        // and
        verify(userRepository).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenCreateUser_ifUserWithUsernameExists() {
        // given
        String username = "test_username";

        User existingUser = User.builder()
                .username(username)
                .build();

        NewUserDTO newUserDTO = NewUserDTO.builder()
                .username(username)
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        // when
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.create(newUserDTO);
        });

        // then
        assertEquals("User with the username test_username already exists!", exception.getMessage());

        // and
        verify(userRepository).findByUsername(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllFriends_whenGetAllFriendsByUserId_ifUserExists() {
        // given
        User user = User.builder().id(USER_ID).build();

        Long friendId = 2L;
        User friend = User.builder().id(friendId).build();
        UserDTO friendDTO = UserDTO.toUserDTO(user);

        when(userRepository.findFriendsById(any())).thenReturn(List.of(friend));

        // when
        List<UserDTO> foundFriends = userService.getAllFriendsById(USER_ID);

        // then
        assertEquals(List.of(friendDTO), foundFriends);

        // and
        verify(userRepository).findFriendsById(any());
    }

    @Test
    void shouldReturnUser_whenFindUserById_ifUserExists() {
        // given
        User user = User.builder().id(USER_ID).build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.findById(USER_ID);

        // then
        assertEquals(user, foundUser);

        // and
        verify(userRepository).findById(any());
    }

    @Test
    void shouldThrowNotFoundException_whenFindUserById_ifUserWithIdDoesntExist() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userService.findById(USER_ID);
        });

        // then
        assertEquals("User with id 1 doesn't exist!", exception.getMessage());

        // and
        verify(userRepository).findById(any());
    }

}
