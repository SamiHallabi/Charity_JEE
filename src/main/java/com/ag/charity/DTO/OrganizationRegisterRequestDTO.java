package com.ag.charity.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRegisterRequestDTO {

    @NotBlank(message = "First name is required")
    private String orgName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String orgEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String orgPassword;

    private String orgDescription;
    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @NotBlank(message = "Legal address is required")
    private String legalAddress;


}
