package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.internship.socialnetwork.dto.NewUserDTO.toUser;
import static com.internship.socialnetwork.dto.UserDTO.toUserDTO;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final String USER_WITH_EMAIL_ALREADY_EXISTS_MESSAGE = "User with email %s already exists!";

    private final String USER_WITH_USERNAME_ALREADY_EXISTS_MESSAGE = "User with username %s already exists!";

    private final UserRepository userRepository;

    @Override
    public UserDTO create(NewUserDTO newUserDTO) {
        checkIfUserExists(newUserDTO);
        return toUserDTO(userRepository.save(toUser(newUserDTO)));
    }

    @Override
    public List<UserDTO> getAllFriendsById(Long id) {
        return userRepository.findFriendsById(id)
                .stream()
                .map(UserDTO::toUserDTO)
                .toList();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s doesn't exist!", id)));
    }

    @Override
    public UserDTO get(Long id) {
        return toUserDTO(findById(id));
    }

    @Override
    public UserDTO update(Long id, NewUserDTO updatedUser) {
        checkIfUserExists(id, updatedUser);
        return toUserDTO(updateUser(findById(id), updatedUser));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

    @Override
    public List<UserDTO> search(String username, String firstName, String lastName) {
        return userRepository.findByUsernameOrFirstNameOrLastName(username, firstName, lastName).stream()
                .map(UserDTO::toUserDTO)
                .toList();
    }


    private User updateUser(User user, NewUserDTO updatedUser) {
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
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

    private void checkIfUserExists(Long id, NewUserDTO newUserDTO) {
        userRepository.findByEmail(newUserDTO.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new BadRequestException(String.format(USER_WITH_EMAIL_ALREADY_EXISTS_MESSAGE, newUserDTO.getEmail()));
            }
        });

        userRepository.findByUsername(newUserDTO.getUsername()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new BadRequestException(String.format(USER_WITH_USERNAME_ALREADY_EXISTS_MESSAGE, newUserDTO.getUsername()));
            }
        });
    }


}
