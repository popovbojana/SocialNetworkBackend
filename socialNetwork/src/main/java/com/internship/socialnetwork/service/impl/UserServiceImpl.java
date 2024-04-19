package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.UpdateUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.internship.socialnetwork.dto.UserDTO.toUserDTO;
import static com.internship.socialnetwork.model.enumeration.Role.USER;
import static com.internship.socialnetwork.model.enumeration.Status.OFFLINE;
import static com.internship.socialnetwork.model.enumeration.Status.ONLINE;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final String USER_WITH_EMAIL_ALREADY_EXISTS_MESSAGE = "User with email %s already exists!";

    private final String USER_WITH_USERNAME_ALREADY_EXISTS_MESSAGE = "User with username %s already exists!";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(NewUserDTO newUserDTO) {
        checkIfUserExists(newUserDTO);
        return toUserDTO(userRepository.save(toUser(newUserDTO)), 0, 0);
    }

    @Override
    public List<UserDTO> getAllFriendsById(Long id) {
        return userRepository.findFriendsById(id)
                .stream()
                .map((user -> UserDTO.toUserDTO(user, getPostsCount(id), getFriendsCount(id))))
                .toList();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s doesn't exist!", id)));
    }

    @Override
    public UserDTO get(Long id) {
        return toUserDTO(findById(id), getPostsCount(id), getFriendsCount(id));
    }

    @Override
    public UserDTO update(Long id, UpdateUserDTO updatedUser) {
        checkIfUserExists(id, updatedUser);
        return toUserDTO(updateUser(findById(id), updatedUser), getPostsCount(id), getFriendsCount(id));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

    @Override
    public List<UserDTO> search(String search) {
        return userRepository.searchForUsers(search).stream()
                .map((user -> UserDTO.toUserDTO(user, getPostsCount(user.getId()), getFriendsCount(user.getId()))))
                .toList();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s doesn't exist!", username)));
    }

    @Override
    public void connect(User user) {
        User storedUser = findById(user.getId());
        storedUser.setStatus(ONLINE);
        userRepository.save(storedUser);
    }

    @Override
    public void disconnect(User user) {
        User storedUser = findById(user.getId());
        storedUser.setStatus(OFFLINE);
        userRepository.save(storedUser);
    }

    @Override
    public List<UserDTO> findConnectedUsers() {
        return userRepository.findAllByStatus(ONLINE).stream()
                .map((user -> UserDTO.toUserDTO(user, getPostsCount(user.getId()), getFriendsCount(user.getId()))))
                .toList();
    }

    private User updateUser(User user, UpdateUserDTO updatedUser) {
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(user);
    }

    private void checkIfUserExists(NewUserDTO newUserDTO) {
        userRepository.findByEmail(newUserDTO.getEmail())
                .ifPresent(user -> {
                    throw new BadRequestException(String.format(USER_WITH_EMAIL_ALREADY_EXISTS_MESSAGE, user.getEmail()));
                });
        userRepository.findByUsername(newUserDTO.getUsername())
                .ifPresent(user -> {
                    throw new BadRequestException(String.format(USER_WITH_USERNAME_ALREADY_EXISTS_MESSAGE, user.getUsername()));
                });
    }

    private void checkIfUserExists(Long id, UpdateUserDTO updatedUser) {
        userRepository.findByEmail(updatedUser.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new BadRequestException(String.format(USER_WITH_EMAIL_ALREADY_EXISTS_MESSAGE, updatedUser.getEmail()));
            }
        });

        userRepository.findByUsername(updatedUser.getUsername()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new BadRequestException(String.format(USER_WITH_USERNAME_ALREADY_EXISTS_MESSAGE, updatedUser.getUsername()));
            }
        });
    }

    private User toUser(NewUserDTO newUserDTO) {
        return User.builder()
                .email(newUserDTO.getEmail())
                .firstName(newUserDTO.getFirstName())
                .lastName(newUserDTO.getLastName())
                .username(newUserDTO.getUsername())
                .password(passwordEncoder.encode(newUserDTO.getPassword()))
                .role(USER)
                .status(ONLINE)
                .phoneNumber(newUserDTO.getPhoneNumber())
                .build();
    }

    private int getPostsCount(Long id) {
        return userRepository.findPostsCountById(id);
    }

    private int getFriendsCount(Long id) {
        return userRepository.findFriendsCountById(id);
    }

}
