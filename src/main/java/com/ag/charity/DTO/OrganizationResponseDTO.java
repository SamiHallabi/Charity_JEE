package com.ag.charity.DTO;

import com.ag.charity.entities.enums.OrganizationStatus;
import com.ag.charity.entities.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class OrganizationResponseDTO {
    private Long id;
    private String orgName;
    private String orgEmail;
    private String taxId;
    private String legalAddress;
    private String orgDescription ;
    private OrganizationStatus status;
    private LocalDateTime createdAt;
}
