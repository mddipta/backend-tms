package com.lawencon.ticket.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.request.user.UpdateUserRequest;
import com.lawencon.ticket.model.request.user.UserCreateRequest;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.user.UserResponse;
import com.lawencon.ticket.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "User", description = "User API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class UserController {
    private UserService userService;


    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<UserResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ResponseHelper.ok(userService.getById(id)));
    }

    @GetMapping(value = "/users/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<UserResponse>>> getByRoleId(@PathVariable String role) {
        return ResponseEntity.ok(ResponseHelper.ok(userService.getAllByRole(role)));
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> create(@RequestBody UserCreateRequest request) {
        userService.create(request);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> update(@RequestBody UpdateUserRequest request) {
        userService.update(request);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ResponseHelper.ok(userService.getAll()));
    }

    @DeleteMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok(ResponseHelper.ok("Success"));
    }
}
