package com.lawencon.ticket.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.ticket.helper.ResponseHelper;
import com.lawencon.ticket.model.response.WebResponse;
import com.lawencon.ticket.model.response.role.RoleResponse;
import com.lawencon.ticket.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Role", description = "Role API endpoint")
@RestController
@RequestMapping({"/api/v1"})
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<RoleResponse>>> findAll() {
        return ResponseEntity.ok(ResponseHelper.ok(roleService.findAll()));
    }

    @GetMapping(value = "/role/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<RoleResponse>> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(ResponseHelper.ok(roleService.findByCode(code)));
    }
}
