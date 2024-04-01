package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.AuthenticationRequestDTO;
import com.internship.socialnetwork.dto.AuthenticationResponseDTO;
import com.internship.socialnetwork.exception.UnauthorizedException;
import com.internship.socialnetwork.service.AuthService;
import com.internship.socialnetwork.service.CommentService;
import com.internship.socialnetwork.service.JwtService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final PostService postService;

    private final CommentService commentService;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO requestDTO) {
        String requestUsername = requestDTO.getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestUsername,
                        requestDTO.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(userService.findByUsername(requestUsername).getUsername());
        return AuthenticationResponseDTO.builder()
                .token(jwtService.generateToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .build();
    }

    @Override
    public boolean hasAccess(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).getId().equals(id);
    }

    @Override
    public boolean hasAccessForPost(Long postId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).equals(postService.findById(postId).getPostedBy());
    }

    @Override
    public boolean hasAccessForComment(Long commentId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).equals(commentService.findById(commentId).getCommentedBy());
    }

    @Override
    public boolean hasAccess(Long fromUserId, Long toUserId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userService.findByUsername(userDetails.getUsername()).getId();
        return currentUserId.equals(fromUserId) || currentUserId.equals(toUserId);
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        var userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            return AuthenticationResponseDTO.builder()
                    .token(jwtService.generateToken(userDetailsService.loadUserByUsername(username)))
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UnauthorizedException("Unauthorized");
        }

    }

}
