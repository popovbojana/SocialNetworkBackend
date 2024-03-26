package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;

import java.util.List;

public interface UserService {

    UserDTO addNewUser(NewUserDTO newUser);

    List<UserDTO> getAllFriends(Long id);

}
