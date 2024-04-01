package com.internship.socialnetwork.service;

import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static com.internship.socialnetwork.model.enumeration.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    private static final Long USER_ID = 1L;

    private static final String USERNAME = "test_username";

    private static final String EMAIL = "test@email.com";

    private static final String PASSWORD = "testPassword123!";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldReturnUserDetails_whenLoadUserByUsername_ifUserExists() {
        // given
        User user = createUser();
        UserDetails userDetails = createUserDetails(user);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        UserDetails foundUserDetails = userDetailsService.loadUserByUsername(USERNAME);

        // then
        assertEquals(userDetails, foundUserDetails);

        // and
        verify(userRepository).findByUsername(any());
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
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

}
