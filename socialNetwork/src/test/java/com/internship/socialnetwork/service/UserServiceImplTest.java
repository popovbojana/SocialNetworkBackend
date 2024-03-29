package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.internship.socialnetwork.dto.UserDTO.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.beans.BeanUtils.copyProperties;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Long USER_ID = 1L;

    private static final String USERNAME = "test_username";

    private static final String EMAIL = "test@email.com";

    private static final String USER_WITH_EMAIL_EXISTS_MESSAGE = "User with email test@email.com already exists!";

    private static final String USER_WITH_USERNAME_EXISTS_MESSAGE = "User with username test_username already exists!";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldAddNewUser() {
        // given
        NewUserDTO newUserDTO = createNewUserDTO();
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
    void shouldThrowBadRequestException_whenAddNewUser_ifUserWithEmailExists() {
        // given
        User existingUser = createUser(USER_ID);
        NewUserDTO newUserDTO = createNewUserDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.create(newUserDTO);
        });

        // then
        assertEquals(USER_WITH_EMAIL_EXISTS_MESSAGE, exception.getMessage());

        // and
        verify(userRepository).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenAddNewUser_ifUserWithUsernameExists() {
        // given
        User existingUser = createUser(USER_ID);
        NewUserDTO newUserDTO = createNewUserDTO();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.create(newUserDTO);
        });

        // then
        assertEquals(USER_WITH_USERNAME_EXISTS_MESSAGE, exception.getMessage());

        // and
        verify(userRepository).findByUsername(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGetAllFriendsById() {
        // given
        User user = createUser(USER_ID);
        User friend = createUser(2L);
        UserDTO friendDTO = toUserDTO(user);

        when(userRepository.findFriendsById(any())).thenReturn(List.of(friend));

        // when
        List<UserDTO> foundFriends = userService.getAllFriendsById(USER_ID);

        // then
        assertEquals(List.of(friendDTO), foundFriends);

        // and
        verify(userRepository).findFriendsById(any());
    }

    @Test
    void shouldFindUserById() {
        // given
        User user = createUser(USER_ID);

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
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.findById(USER_ID);
        });

        // then
        assertEquals("User with id 1 doesn't exist!", exception.getMessage());

        // and
        verify(userRepository).findById(any());
    }

    @Test
    void shouldReturnUser_whenGetUser_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        UserDTO userDTO = toUserDTO(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserDTO foundUserDTO = userService.get(USER_ID);

        // then
        assertEquals(userDTO, foundUserDTO);

        // and
        verify(userRepository).findById(any());
    }

    @Test
    void shouldReturnUser_whenUpdateUser_ifUserExists() {
        // given
        String newUsername = "new_username";
        User user = createUser(USER_ID);
        NewUserDTO updates = createNewUserDTO();
        updates.setUsername(newUsername);
        User updatedUser = createUser(USER_ID);
        updatedUser.setUsername(newUsername);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(updatedUser);

        // when
        UserDTO updatedUserDTO = userService.update(USER_ID, updates);

        // then
        assertEquals(toUserDTO(updatedUser), updatedUserDTO);

        // and
        verify(userRepository).findById(any());
        verify(userRepository).findByUsername(any());
        verify(userRepository).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenUpdateUser_ifUserWithEmailExists() {
        // given
        NewUserDTO updates = createNewUserDTO();
        User otherUser = createUser(2L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(otherUser));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.update(USER_ID, updates);
        });

        // then
        assertEquals(USER_WITH_EMAIL_EXISTS_MESSAGE, exception.getMessage());

        // and
        verify(userRepository).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenUpdateUser_ifUserWithUsernameExists() {
        // given
        NewUserDTO updates = createNewUserDTO();
        User otherUser = createUser(2L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(otherUser));

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.update(USER_ID, updates);
        });

        // then
        assertEquals(USER_WITH_USERNAME_EXISTS_MESSAGE, exception.getMessage());

        // and
        verify(userRepository).findByEmail(any());
        verify(userRepository).findByUsername(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUser_whenDeleteUser_IfUserExists() {
        // given
        User user = createUser(USER_ID);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        userService.delete(USER_ID);

        // then
        verify(userRepository).findById(any());
        verify(userRepository).delete(any());
    }

    @Test
    void shouldReturnUsers_whenSearch_ifUsersExist() {
        // given
        User user = createUser(USER_ID);

        when(userRepository.findByUsernameOrFirstNameOrLastName(any(), any(), any())).thenReturn(List.of(user));

        // when
        List<UserDTO> foundUsers = userService.search(USERNAME, null, null);

        // then
        assertEquals(List.of(toUserDTO(user)), foundUsers);

        // and
        verify(userRepository).findByUsernameOrFirstNameOrLastName(any(), any(), any());
    }

    private User createUser(Long userId) {
        return User.builder()
                .id(userId)
                .email(EMAIL)
                .username(USERNAME)
                .build();
    }

    private NewUserDTO createNewUserDTO() {
        return NewUserDTO.builder()
                .email(EMAIL)
                .username(USERNAME)
                .build();
    }

}
