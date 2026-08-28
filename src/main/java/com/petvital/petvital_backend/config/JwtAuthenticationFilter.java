package com.petvital.petvital_backend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.petvital.petvital_backend.feature.user.User;
import com.petvital.petvital_backend.feature.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_HEADER = "X-Refresh-Token";
    private static final String NEW_ACCESS_TOKEN_HEADER = "X-New-Access-Token";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * The filter is skipped for the auth endpoints and for CORS pre-flight
     * OPTIONS requests, so they do not require any token.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = extractBearerToken(request);
        String refreshToken = request.getHeader(REFRESH_HEADER);

        boolean accessValid =
                accessToken != null && jwtService.isTokenValid(accessToken);
        boolean refreshValid =
                refreshToken != null && jwtService.isTokenValid(refreshToken);

        if (!accessValid && !refreshValid) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid token. " +
                    "Provide a valid Access Token in the Authorization header " +
                    "and/or a valid Refresh Token in the X-Refresh-Token header.");
            return;
        }

        String email = accessValid
                ? jwtService.extractUsername(accessToken)
                : jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User associated with the token no longer exists.");
            return;
        }

        // If the access token is missing or expired but the refresh token is
        // valid, allow the request and issue a fresh access token so the
        // client can store it for the next call.
        if (!accessValid && refreshValid) {
            String newAccessToken = jwtService.generateAccessToken(user);
            response.setHeader(NEW_ACCESS_TOKEN_HEADER, newAccessToken);
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user, null, List.of());

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}