package com.lawencon.ticket.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);

    String extractUserName(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
