package com.internship.socialnetwork.service;

import com.internship.socialnetwork.config.ApplicationConfig;
import com.internship.socialnetwork.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    private final String USERNAME = "test_username";

    private final String PASSWORD = "Password123!";

    private final String SECRET_KEY = "S5Ybr1gXoRfMjcBGHtdEkwTFc+y9diC7Vq+adfYQino=";

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    void shouldReturnUsername_whenExtractUsername_IfValidIsToken() {
        // given
        String token = createToken(new Date());

        when(applicationConfig.getSecretKey()).thenReturn(SECRET_KEY);

        // when
        String extractedUsername = jwtService.extractUsername(token);

        // then
        assertEquals(USERNAME, extractedUsername);

        // and
        verify(applicationConfig).getSecretKey();
    }

    @Test
    void shouldReturnToken_whenGenerateToken_ifUserExists() {
        // given
        UserDetails userDetails = createUserDetails();

        when(applicationConfig.getSecretKey()).thenReturn(SECRET_KEY);
        when(applicationConfig.getTokenExpiration()).thenReturn(43200000);

        // when
        String token = jwtService.generateToken(userDetails);

        // then
        assertNotNull(token);

        // and
        verify(applicationConfig).getSecretKey();
        verify(applicationConfig).getTokenExpiration();
    }

    @Test
    void shouldReturnRefreshToken_whenGenerateRefreshToken_ifUserExists() {
        // given
        UserDetails userDetails = createUserDetails();

        when(applicationConfig.getSecretKey()).thenReturn(SECRET_KEY);
        when(applicationConfig.getRefreshTokenExpiration()).thenReturn(432000000);

        // when
        String token = jwtService.generateRefreshToken(userDetails);

        // then
        assertNotNull(token);

        // and
        verify(applicationConfig).getSecretKey();
        verify(applicationConfig).getRefreshTokenExpiration();
    }

    @Test
    void shouldReturnTrue_whenIsTokenValid_ifTokenIsValid() {
        // given
        String token = createToken(new Date());
        UserDetails userDetails = createUserDetails();

        when(applicationConfig.getSecretKey()).thenReturn(SECRET_KEY);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
        assertTrue(isValid);

        // and
        verify(applicationConfig, atLeastOnce()).getSecretKey();
    }

    private String createToken(Date issuedAt) {
        return Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + TimeUnit.DAYS.toMillis(11)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)), SignatureAlgorithm.HS256)
                .compact();
    }

    private UserDetails createUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                USERNAME, PASSWORD, Collections.emptyList());
    }
}
