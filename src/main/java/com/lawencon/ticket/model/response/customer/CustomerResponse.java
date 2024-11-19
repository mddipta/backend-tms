package com.lawencon.ticket.model.response.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private String id;
    private String company;
    private String companyId;
    private String user;
    private String userId;
    private String name;
    private Long version;
    private String picName;
    private String picId;
    private Boolean isActive;
}
