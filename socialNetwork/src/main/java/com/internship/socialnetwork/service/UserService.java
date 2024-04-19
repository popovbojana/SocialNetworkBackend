package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.UpdateUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.model.User;

import java.util.List;

public interface UserService {

    UserDTO create(NewUserDTO newUser);

    List<UserDTO> getAllFriendsById(Long id);

    User findById(Long id);

    UserDTO get(Long id);

    UserDTO update(Long id, UpdateUserDTO updatedUser);

    void delete(Long id);

    List<UserDTO> search(String search);

    User findByUsername(String username);

    void connect(User user);

    void disconnect(User user);

    List<UserDTO> findConnectedUsers();

}
