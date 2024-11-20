package com.lawencon.ticket.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lawencon.ticket.authentication.model.UserPrinciple;
import com.lawencon.ticket.model.response.role.RoleResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lawencon.ticket.model.request.user.UpdateUserRequest;
import com.lawencon.ticket.model.request.user.UserCreateRequest;
import com.lawencon.ticket.model.request.user.UserLoginRequest;
import com.lawencon.ticket.model.response.user.UserResponse;
import com.lawencon.ticket.persistence.entity.Role;
import com.lawencon.ticket.persistence.entity.User;
import com.lawencon.ticket.persistence.repository.UserRepository;
import com.lawencon.ticket.service.RoleService;
import com.lawencon.ticket.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> login(UserLoginRequest request) {
        Optional<User> user = repository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Wrong username or password");
            }
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong username or password");
        }
    }

    @Override
    public List<UserResponse> getAll() {
        List<UserResponse> responses = new ArrayList<>();
        List<User> users = repository.findAll();
        for (User user : users) {
            UserResponse response = mapToResponse(user);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public UserResponse getById(String id) {
        User user = findEntityById(id);
        return mapToResponse(user);
    }

    @Override
    public void create(UserCreateRequest request) {
        validateUsernameExist(request.getUsername());
        validateEmailExist(request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        Role role = roleService.findById(request.getRoleId());
        role.setId(role.getId());
        user.setRole(role);

        user.setIsActive(true);

        repository.save(user);
    }

    @Override
    public void update(UpdateUserRequest request) {
        User user = repository.findById(request.getId()).get();
        if (!request.getUsername().equals(user.getUsername())) {
            validateUsernameExist(request.getUsername());
        }

        if (!request.getEmail().equals(user.getEmail())) {
            validateEmailExist(request.getEmail());
        }

        if (!request.getVersion().equals(user.getVersion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Version not match");
        }

        if (!request.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        validateIdExist(request.getId());

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setIsActive(request.getIsActive());
        user.setVersion(request.getVersion() + 1);

        Role role = roleService.findById(request.getRoleId());
        role.setId(role.getId());
        user.setRole(role);

        repository.saveAndFlush(user);
    }

    @Override
    public void delete(String id) {
        validateIdExist(id);
        User user = repository.findById(id).get();
        user.setDeletedAt(ZonedDateTime.now(ZoneOffset.UTC));
        repository.delete(user);
    }

    @Override
    public User findEntityById(String id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
        }
        return user.get();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                User user = repository.findByUsername(username)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "User not found"));
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(user.getRole().getCode()));
                return UserPrinciple.builder().user(user).authorities(authorities)
                        .role(user.getRole()).build();
            }
        };
    }

    @Override
    public List<UserResponse> getAllByRole(String role) {
        List<UserResponse> responses = new ArrayList<>();
        RoleResponse roleEntity = roleService.findByCode(role);
        List<User> users = repository.findByRoleId(roleEntity.getId());
        for (User user : users) {
            UserResponse response = mapToResponse(user);
            responses.add(response);
        }
        return responses;
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setRole(user.getRole().getCode());
        response.setRoleName(user.getRole().getName());
        response.setRoleId(user.getRole().getId());
        BeanUtils.copyProperties(user, response);
        return response;
    }

    private void validateUsernameExist(String username) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
    }

    private void validateIdExist(String id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id not found");
        }
    }

    private void validateEmailExist(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
    }

}
