package com.training.portal.controller;

import com.training.portal.config.security.JwtUtils;
import com.training.portal.dto.request.AuthRequest;
import com.training.portal.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final String defaultUsername;
    private final String defaultPassword;
    private final JwtUtils jwtUtils;

    public AuthController(
            @Value("${auth.default-username:admin}") String defaultUsername,
            @Value("${auth.default-password:password}") String defaultPassword,
            JwtUtils jwtUtils) {
        this.defaultUsername = defaultUsername;
        this.defaultPassword = defaultPassword;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and return a token")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        if (!defaultUsername.equals(request.username()) || !defaultPassword.equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        String token = jwtUtils.generateToken(request.username());
        return new AuthResponse(token);
    }
}
