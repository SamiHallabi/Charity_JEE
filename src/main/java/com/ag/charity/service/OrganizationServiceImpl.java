package com.ag.charity.service;

import com.ag.charity.DTO.*;
import com.ag.charity.entities.enums.OrganizationStatus;

import com.ag.charity.entities.jpa.Organization;
import com.ag.charity.repositories.jpa.OrganizationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OrganizationResponseDTO register(OrganizationRegisterRequestDTO dto) {
        if (organizationRepository.existsByOrgEmail(dto.getOrgEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        Organization organization = new Organization();
        organization.setOrgName(dto.getOrgName());
        organization.setOrgEmail(dto.getOrgEmail());
        organization.setOrgDescription(dto.getOrgDescription());
        organization.setLegalAddress(dto.getLegalAddress());
        organization.setTaxId(dto.getTaxId());
        organization.setOrgPassword(passwordEncoder.encode(dto.getOrgPassword()));
        organization.setStatus(OrganizationStatus.PENDING);

        Organization savedOrganization = organizationRepository.save(organization);
        return toDTO(savedOrganization);
    }

    @Override
    public OrganizationResponseDTO getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Organisation non trouvé"));
        return toDTO(organization);
    }

    @Override
    public OrganizationResponseDTO updateOrganizationProfile(Long id, OrganizationProfileUpdateDTO dto) {

        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Organisation non trouvé"));
        if (dto.getLegalAddress() != null) {
            organization.setLegalAddress(dto.getLegalAddress());
        }
        if (dto.getOrgDescription() != null) {
            organization.setOrgDescription(dto.getOrgDescription());
        }
        if (dto.getOrgPassword() != null) {
            organization.setOrgPassword(passwordEncoder.encode(dto.getOrgPassword()));
        }
        return toDTO(organizationRepository.save(organization));
    }

    @Override
    public void deleteOrganization(Long id) {

        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organisation non trouvé");
        }
        organizationRepository.deleteById(id);

    }

    @Override
    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private OrganizationResponseDTO toDTO(Organization organization) {
        OrganizationResponseDTO dto = new OrganizationResponseDTO();
        dto.setId(organization.getId());
        dto.setOrgName(organization.getOrgName());
        dto.setOrgEmail(organization.getOrgEmail());
        dto.setOrgDescription(organization.getOrgDescription());
        dto.setStatus(organization.getStatus());
        dto.setCreatedAt(organization.getCreatedAt() != null ? LocalDateTime.parse(organization.getCreatedAt().toString()) : null);
        return dto;
    }
}
