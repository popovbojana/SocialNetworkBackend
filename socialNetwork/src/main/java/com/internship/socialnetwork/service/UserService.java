package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;

public interface UserService {

    UserDTO addNewUser(NewUserDTO newUser);

}
