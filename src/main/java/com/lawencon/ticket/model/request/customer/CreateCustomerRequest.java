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
public class CreateCustomerRequest {
    @NotBlank(message = "User cannot be empty")
    private String userId;

    @NotBlank(message = "Company cannot be empty")
    private String companyId;

    @NotBlank(message = "PIC cannot be empty")
    private String picId;
}
