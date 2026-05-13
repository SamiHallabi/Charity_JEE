package com.ag.charity.service;

import com.ag.charity.DTO.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizationService {
    OrganizationResponseDTO register(OrganizationRegisterRequestDTO dto);
    OrganizationResponseDTO getOrganizationById(Long id);
    OrganizationResponseDTO updateOrganizationProfile(Long id, OrganizationProfileUpdateDTO dto);
    void deleteOrganization(Long id);
    List<OrganizationResponseDTO> getAllOrganizations();
}
