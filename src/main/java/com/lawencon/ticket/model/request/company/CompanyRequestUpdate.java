package com.lawencon.ticket.model.request.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestUpdate {
    @NotBlank(message = "Id cannot be empty")
    private String id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Version cannot be empty")
    private Boolean isActive;

    @NotBlank(message = "Version cannot be empty")
    private Long version;
}
