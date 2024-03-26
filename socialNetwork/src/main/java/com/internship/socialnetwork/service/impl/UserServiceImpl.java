package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.FriendRequestRepository;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FriendRequestRepository friendRequestRepository;

    @Override
    public UserDTO addNewUser(NewUserDTO newUserDTO) {
        checkIfUserExists(newUserDTO);
        return UserDTO.toUserDTO(userRepository.save(NewUserDTO.mapToUser(newUserDTO)));
    }

    @Override
    public List<UserDTO> getAllFriends(Long id) {
        return userRepository.findFriendsById(id)
                .stream()
                .map(UserDTO::toUserDTO)
                .toList();
    }

    private void checkIfUserExists(NewUserDTO newUserDTO) {
        userRepository.findByEmail(newUserDTO.getEmail())
                .ifPresent(user -> {
                    throw new BadRequestException(String.format("User with the email %s already exists!", user.getEmail()));
                });
        userRepository.findByUsername(newUserDTO.getUsername())
                .ifPresent(user -> {
                    throw new BadRequestException(String.format("User with the username %s already exists!", user.getUsername()));
                });
    }
}
