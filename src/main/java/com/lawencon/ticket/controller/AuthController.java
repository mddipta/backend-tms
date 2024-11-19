package com.lawencon.ticket.controller;

import com.lawencon.ticket.authentication.service.AuthenticationService;
import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.request.user.UserLoginRequest;
import com.lawencon.ticket.model.response.JwtAuthenticationResponse;
import com.lawencon.ticket.model.response.WebResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Comment API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<JwtAuthenticationResponse>> login(
            @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(ResponseHelper.ok(authenticationService.login(request)));
    }
}
