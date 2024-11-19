package com.lawencon.ticket.authentication.service.impl;

import com.lawencon.ticket.authentication.service.AuthenticationService;
import com.lawencon.ticket.authentication.service.JwtService;
import com.lawencon.ticket.model.request.user.UserLoginRequest;
import com.lawencon.ticket.model.response.JwtAuthenticationResponse;
import com.lawencon.ticket.persistence.entity.User;
import com.lawencon.ticket.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public JwtAuthenticationResponse login(UserLoginRequest loginRequest) {
        Optional<User> userOpt = userService.login(loginRequest);

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user is not exist");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userPrinciple =
                userService.userDetailsService().loadUserByUsername(loginRequest.getUsername());
        String token = jwtService.generateToken(userPrinciple);

        return JwtAuthenticationResponse.builder().token(token).userId(userOpt.get().getId()).role(userOpt.get().getRole().getCode()).build();
    }
}
