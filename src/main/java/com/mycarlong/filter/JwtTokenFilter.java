package com.mycarlong.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mycarlong.config.JWTUtil;
import com.mycarlong.dto.CustomOAuth2User;
import com.mycarlong.dto.UserDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final JWTUtil jwtUtil;

    public JwtTokenFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                } else if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (authorization == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorization != null && jwtUtil.validateToken(authorization)) {
            authenticateWithToken(authorization);
        } else if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            if (authorization != null) {
                try {
                    reAuthenticate(refreshToken, authorization);
                } catch (Exception e) {
                    logger.error("Failed to re-authenticate with refresh token", e);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token or authorization token");
                    return;
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateWithToken(String token) {
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        if (username != null && role != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(username);
            userDTO.setRole(role);

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("Authenticated user: " + username);
        }
    }

    private void reAuthenticate(String refreshToken, String authorization) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        if (username.equals(jwtUtil.getUsername(authorization)) && role.equals(jwtUtil.getRole(authorization))) {
            authenticateWithToken(refreshToken);
        } else {
            throw new IllegalArgumentException("User information mismatch between refresh and authorization token");
        }
    }
}