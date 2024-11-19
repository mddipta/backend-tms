package com.lawencon.ticket.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class MasterEntity extends DeletableEntity {
    @Column(name = "is_active")
    private Boolean isActive;
}
