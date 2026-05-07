package com.ag.charity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileUpdateDTO {

    private String orgName;
    private String orgDescription;
    private String orgEmail;
    private String orgPassword;
    private String taxId;
    private String legalAddress;

}
