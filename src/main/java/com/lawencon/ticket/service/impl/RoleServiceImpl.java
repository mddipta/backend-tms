package com.lawencon.ticket.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lawencon.ticket.model.response.role.RoleResponse;
import com.lawencon.ticket.persistence.entity.Role;
import com.lawencon.ticket.persistence.repository.RoleRepository;
import com.lawencon.ticket.service.RoleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public List<RoleResponse> findAll() {
        List<Role> roleList = repository.findAll();
        List<RoleResponse> roleResponse = new ArrayList<>();
        for (Role role : roleList) {
            RoleResponse response = mapToResponse(role);
            roleResponse.add(response);
        }
        return roleResponse;
    }

    @Override
    public RoleResponse findByCode(String code) {
        Optional<Role> role = repository.findByCode(code);
        if (role.isPresent()) {
            return mapToResponse(role.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Role findById(String id) {
        Optional<Role> role = repository.findById(id);
        if (role.isPresent()) {
            return role.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

   private RoleResponse mapToResponse(Role role) {
        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(role, response);
         return response;
    }



}
