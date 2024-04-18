package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.AuthenticationRequestDTO;
import com.internship.socialnetwork.dto.AuthenticationResponseDTO;
import com.internship.socialnetwork.exception.UnauthorizedException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static com.internship.socialnetwork.model.enumeration.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    private static final Long USER_ID = 1L;

    private static final Long OTHER_USER_ID = 2L;

    private static final String USERNAME = "test_username";

    private static final String EMAIL = "test@email.com";

    private static final String PASSWORD = "testPassword123!";

    private static final String TOKEN = "test_token";

    private static final Long POST_ID = 1L;

    private static final String COMMENT = "Test comment.";

    private static final String REFRESH_TOKEN = "refresh_token";

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldReturnAuthenticationResponse_whenLogin_ifUserExists() {
        // given
        User user = createUser(USER_ID);
        UserDetails userDetails = createUserDetails(user);
        AuthenticationRequestDTO request = createAuthenticationRequest();
        AuthenticationResponseDTO response =  createAuthenticationResponse();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userService.findByUsername(any())).thenReturn(user);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtService.generateToken(any(), any())).thenReturn(TOKEN);

        // when
        AuthenticationResponseDTO responseDTO = authService.login(request);

        // then
        assertEquals(response, responseDTO);

        // and
        verify(authenticationManager).authenticate(any());
        verify(userService, atLeastOnce()).findByUsername(any());
        verify(userDetailsService).loadUserByUsername(any());
        verify(jwtService).generateToken(any(), any());
    }

    @Test
    void shouldReturnTrue_whenHasAccess_ifUserHasAccess() {
        // given
        User user = createUser(USER_ID);
        UserDetails userDetails = createUserDetails(user);

        addUserToContext(userDetails);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccess(USER_ID);

        // then
        assertTrue(found);

        // and
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnFalse_whenHasAccess_ifUserDoesntHaveAccess() {
        // given
        User otherUser = createUser(OTHER_USER_ID);
        UserDetails userDetails = createUserDetails(otherUser);

        addUserToContext(userDetails);
        when(userService.findByUsername(any())).thenReturn(otherUser);

        // when
        boolean found = authService.hasAccess(USER_ID);

        // then
        assertFalse(found);

        // and
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnTrue_whenHasAccessForPost_ifUserHasAccess() {
        // given
        User user = createUser(USER_ID);
        UserDetails userDetails = createUserDetails(user);
        Post post = createPostByIdAndUser(user);

        addUserToContext(userDetails);
        when(postService.findById(any())).thenReturn(post);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccessForPost(USER_ID);

        // then
        assertTrue(found);

        // and
        verify(postService).findById(any());
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnFalse_whenHasAccessForPost_ifUserDoesntHaveAccess() {
        // given
        User user = createUser(USER_ID);
        User otherUser = createUser(OTHER_USER_ID);
        UserDetails userDetails = createUserDetails(user);
        Post post = createPostByIdAndUser(otherUser);

        addUserToContext(userDetails);
        when(postService.findById(any())).thenReturn(post);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccessForPost(USER_ID);

        // then
        assertFalse(found);

        // and
        verify(postService).findById(any());
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnTrue_whenHasAccessForComment_ifUserHasAccess() {
        // given
        User user = createUser(USER_ID);
        UserDetails userDetails = createUserDetails(user);
        Post post = createPostByIdAndUser(user);
        Comment comment = createComment(post, user);

        addUserToContext(userDetails);
        when(commentService.findById(any())).thenReturn(comment);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccessForComment(USER_ID);

        // then
        assertTrue(found);

        // and
        verify(commentService).findById(any());
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnFalse_whenHasAccessForComment_ifUserDoesntHaveAccess() {
        // given
        User user = createUser(USER_ID);
        User otherUser = createUser(OTHER_USER_ID);
        UserDetails userDetails = createUserDetails(user);
        Post post = createPostByIdAndUser(otherUser);
        Comment comment = createComment(post, otherUser);

        addUserToContext(userDetails);
        when(commentService.findById(any())).thenReturn(comment);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccessForComment(USER_ID);

        // then
        assertFalse(found);

        // and
        verify(commentService).findById(any());
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnTrue_whenHasAccess_ifOneOfUserHasAccess() {
        // given
        User user = createUser(USER_ID);
        UserDetails userDetails = createUserDetails(user);

        addUserToContext(userDetails);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccess(USER_ID, OTHER_USER_ID);

        // then
        assertTrue(found);

        // and
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnFalse_whenHasAccess_ifOneOfUserHasAccess() {
        // given
        User user = createUser(3L);
        UserDetails userDetails = createUserDetails(user);

        addUserToContext(userDetails);
        when(userService.findByUsername(any())).thenReturn(user);

        // when
        boolean found = authService.hasAccess(USER_ID, OTHER_USER_ID);

        // then
        assertFalse(found);

        // and
        verify(userService).findByUsername(any());
    }

    @Test
    void shouldReturnNewToken_whenRefreshToken_ifRefreshTokenValid() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + REFRESH_TOKEN);
        when(jwtService.extractUsername(any())).thenReturn(USERNAME);
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(jwtService.generateToken(userDetailsService.loadUserByUsername(any()))).thenReturn(TOKEN);

        // Act
        AuthenticationResponseDTO responseDTO = authService.refreshToken(request, response);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(REFRESH_TOKEN, responseDTO.getRefreshToken());

        // Verify
        verify(jwtService).extractUsername(any());
        verify(jwtService).isTokenValid(any(), any());
        verify(jwtService).generateToken(any());
    }

    @Test
    void shouldThrowUnauthorizedException_whenRefreshToken_ifRefreshTokenInvalid() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + REFRESH_TOKEN);
        when(jwtService.extractUsername(any())).thenReturn("non_existing_user");
        when(jwtService.isTokenValid(any(), any())).thenReturn(false);

        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authService.refreshToken(request, response);
        });

        // then
        assertEquals("Unauthorized", exception.getMessage());

        // and
        verify(jwtService).extractUsername(any());
        verify(jwtService).isTokenValid(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .role(USER)
                .build();
    }

    private UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), String.valueOf(user.getPassword()),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

    private void addUserToContext(UserDetails user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private AuthenticationRequestDTO createAuthenticationRequest() {
        return AuthenticationRequestDTO.builder().
                username(USERNAME)
                .password(PASSWORD)
                .build();
    }

    private AuthenticationResponseDTO createAuthenticationResponse() {
        return AuthenticationResponseDTO.builder()
                .token(TOKEN)
                .build();
    }

    private Post createPostByIdAndUser(User user) {
        return Post.builder()
                .id(POST_ID)
                .postedBy(user)
                .build();
    }

    private Comment createComment(Post post, User user) {
        return Comment.builder()
                .post(post)
                .commentedBy(user)
                .comment(COMMENT)
                .build();
    }

}
