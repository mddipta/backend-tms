package com.lawencon.ticket.model.request.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {
    @NotBlank(message = "ID cannot be empty")
    private String id;

    @NotBlank(message = "User cannot be empty")
    private String userId;

    @NotBlank(message = "Company cannot be empty")
    private String companyId;

    @NotBlank(message = "PIC cannot be empty")
    private String picId;

    @NotBlank(message = "Is Active cannot be empty")
    private Boolean isActive;

    @NotBlank(message = "Version cannot be empty")
    private Long version;
}
