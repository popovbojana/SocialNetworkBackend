package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.model.User;

import java.util.List;

public interface UserService {

    UserDTO create(NewUserDTO newUser);

    List<UserDTO> getAllFriendsById(Long id);

    User findById(Long id);

}
