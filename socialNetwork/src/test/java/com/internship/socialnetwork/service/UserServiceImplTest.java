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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.internship.socialnetwork.dto.UserDTO.toUserDTO;
import static com.internship.socialnetwork.model.enumeration.Role.USER;
import static com.internship.socialnetwork.model.enumeration.Status.OFFLINE;
import static com.internship.socialnetwork.model.enumeration.Status.ONLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.beans.BeanUtils.copyProperties;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Long USER_ID = 1L;

    private static final String USERNAME = "test_username";

    private static final String EMAIL = "test@email.com";

    private static final String PASSWORD = "testPassword123!";

    private static final String USER_WITH_EMAIL_EXISTS_MESSAGE = "User with email test@email.com already exists!";

    private static final String USER_WITH_USERNAME_EXISTS_MESSAGE = "User with username test_username already exists!";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUser_whenCreate_ifUserDoesntExist() {
        // given
        NewUserDTO newUserDTO = createNewUserDTO();
        User user = createUser(USER_ID);
        UserDTO userDTO = new UserDTO();

        copyProperties(newUserDTO, user);
        copyProperties(newUserDTO, userDTO);

        when(userRepository.save(any())).thenReturn(user);
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
    void shouldThrowBadRequestException_whenCreate_ifUserWithEmailExists() {
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
    void shouldThrowBadRequestException_whenCreate_ifUserWithUsernameExists() {
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
    void shouldReturnAllFriends_whenGetAllFriendsByUserId_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        User friend = createUser(2L);
        UserDTO friendDTO = toUserDTO(user, 0, 0);

        when(userRepository.findFriendsById(any())).thenReturn(List.of(friend));

        // when
        List<UserDTO> foundFriends = userService.getAllFriendsById(USER_ID);

        // then
        assertEquals(List.of(friendDTO), foundFriends);

        // and
        verify(userRepository, atLeastOnce()).findFriendsById(any());
    }

    @Test
    void shouldReturnUser_whenFindById_ifUserExists() {
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
    void shouldThrowNotFoundException_whenFindById_ifUserWithIdDoesntExist() {
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
    void shouldReturnUser_whenGet_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        UserDTO userDTO = toUserDTO(user, 0, 0);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserDTO foundUserDTO = userService.get(USER_ID);

        // then
        assertEquals(userDTO, foundUserDTO);

        // and
        verify(userRepository).findById(any());
    }

    @Test
    void shouldReturnUser_whenUpdate_ifUserExists() {
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
        assertEquals(toUserDTO(updatedUser, 0, 0), updatedUserDTO);

        // and
        verify(userRepository).findById(any());
        verify(userRepository).findByUsername(any());
        verify(userRepository).save(any());
    }

    @Test
    void shouldThrowBadRequestException_whenUpdate_ifUserWithEmailExists() {
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
    void shouldThrowBadRequestException_whenUpdate_ifUserWithUsernameExists() {
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
    void shouldDeleteUser_whenDelete_IfUserExists() {
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
        assertEquals(List.of(toUserDTO(user, 0, 0)), foundUsers);

        // and
        verify(userRepository).findByUsernameOrFirstNameOrLastName(any(), any(), any());
    }

    @Test
    void shouldReturnUser_whenFindByUsername_ifUserExists() {
        // given
        User user = createUser(USER_ID);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.findByUsername(USERNAME);

        // then
        assertEquals(user, foundUser);

        // and
        verify(userRepository).findByUsername(any());
    }

    @Test
    void shouldThrowNotFoundException_whenFindByUsername_ifUserWithUsernameDoesntExist() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.findByUsername(USERNAME);
        });

        // then
        assertEquals("User with username test_username doesn't exist!", exception.getMessage());

        // and
        verify(userRepository).findByUsername(any());
    }

    @Test
    void shouldSetStatusToOnline_whenConnect_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        user.setStatus(OFFLINE);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        // when
        userService.connect(user);

        // then
        assertEquals(user.getStatus(), ONLINE);

        // and
        verify(userRepository).findById(any());
        verify(userRepository).save(any());
    }

    @Test
    void shouldSetStatusToOffline_whenDisconnect_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        user.setStatus(ONLINE);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        // when
        userService.disconnect(user);

        // then
        assertEquals(user.getStatus(), OFFLINE);

        // and
        verify(userRepository).findById(any());
        verify(userRepository).save(any());
    }

    @Test
    void shouldReturnConnectedUsers_whenFindConnectedUsers_ifConnectedUsersExist() {
        // given
        User connectedUser = createUser(USER_ID);
        connectedUser.setStatus(ONLINE);
        List<UserDTO> connectedUsers = List.of(toUserDTO(connectedUser, 0, 0));

        when(userRepository.findAllByStatus(any())).thenReturn(List.of(connectedUser));

        // when
        List<UserDTO> foundUsers = userService.findConnectedUsers();

        // then
        assertEquals(connectedUsers, foundUsers);

        // and
        verify(userRepository).findAllByStatus(any());
    }

    private User createUser(Long userId) {
        return User.builder()
                .id(userId)
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .posts(List.of())
                .role(USER)
                .build();
    }

    private NewUserDTO createNewUserDTO() {
        return NewUserDTO.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

}
