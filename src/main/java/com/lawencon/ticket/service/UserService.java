package com.lawencon.ticket.service;

import java.util.List;
import java.util.Optional;

import com.lawencon.ticket.model.request.user.UpdateUserRequest;
import com.lawencon.ticket.model.request.user.UserCreateRequest;
import com.lawencon.ticket.model.request.user.UserLoginRequest;
import com.lawencon.ticket.model.response.user.UserResponse;
import com.lawencon.ticket.persistence.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    Optional<User> login(UserLoginRequest request);

    List<UserResponse> getAll();

    UserResponse getById(String id);

    void create(UserCreateRequest request);

    void update(UpdateUserRequest request);

    void delete(String id);

    User findEntityById(String userId);

    UserDetailsService userDetailsService();

    List<UserResponse> getAllByRole(String role);
}
