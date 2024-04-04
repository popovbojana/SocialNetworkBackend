package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.AuthenticationRequestDTO;
import com.internship.socialnetwork.dto.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthenticationResponseDTO login(AuthenticationRequestDTO requestDTO);

    boolean hasAccess(Long id);

    boolean hasAccessForPost(Long postId);

    boolean hasAccessForComment(Long commentId);

    boolean hasAccess(Long fromUserId, Long toUserId);

    AuthenticationResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response);

}
