package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.internship.socialnetwork.dto.NewUserDTO.toUser;
import static com.internship.socialnetwork.dto.UserDTO.toUserDTO;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO create(NewUserDTO newUserDTO) {
        checkIfUserExists(newUserDTO);
        return toUserDTO(userRepository.save(toUser(newUserDTO)));
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
