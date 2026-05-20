package com.ag.charity.service;

import com.ag.charity.DTO.OrganizationResponseDTO;
import com.ag.charity.DTO.UserResponseDTO;

import java.util.List;

public interface AdminService {
    OrganizationResponseDTO approveOrganization(Long organizationId);
    OrganizationResponseDTO rejectOrganization(Long organizationId, String reason);
    List<OrganizationResponseDTO> getPendingOrganizations();
    List<OrganizationResponseDTO> getAllOrganizations();
    List<UserResponseDTO> getAllUsers();
    void deleteUser(Long userId);
}
