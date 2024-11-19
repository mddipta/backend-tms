package com.lawencon.ticket.service;

import java.util.List;

import com.lawencon.ticket.model.response.role.RoleResponse;
import com.lawencon.ticket.persistence.entity.Role;

public interface RoleService {
    List<RoleResponse> findAll();

    RoleResponse findByCode(String code);

    public Role findById(String roleId);
}
