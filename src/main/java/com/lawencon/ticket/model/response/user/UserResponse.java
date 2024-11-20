package com.lawencon.ticket.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String name;
    private String email;
    private String role;
    private Boolean isActive;
    private Long version;
    private String roleName;
    private String roleId;
}
