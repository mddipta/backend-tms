package com.lawencon.ticket.authentication.service;

import com.lawencon.ticket.model.request.user.UserLoginRequest;
import com.lawencon.ticket.model.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse login(UserLoginRequest loginRequest);
}
