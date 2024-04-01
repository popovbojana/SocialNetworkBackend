package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.AuthenticationRequestDTO;
import com.internship.socialnetwork.dto.AuthenticationResponseDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.service.AuthService;
import com.internship.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody NewUserDTO newUser) {
        return new ResponseEntity<>(userService.create(newUser), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDTO) {
        return new ResponseEntity<>(authService.login(requestDTO), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }

}
