package com.ag.charity.service;

import com.ag.charity.DTO.OrganizationResponseDTO;
import com.ag.charity.DTO.UserResponseDTO;
import com.ag.charity.entities.enums.OrganizationStatus;
import com.ag.charity.entities.jpa.Organization;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.OrganizationRepository;
import com.ag.charity.repositories.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AdminServiceImpl(OrganizationRepository organizationRepository,
                            UserRepository userRepository,
                            EmailService emailService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public OrganizationResponseDTO approveOrganization(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        org.setStatus(OrganizationStatus.APPROVED);
        Organization saved = organizationRepository.save(org);
        emailService.sendOrgApprovalEmail(org.getOrgEmail(), org.getOrgName(), true, null);
        return toOrgDTO(saved);
    }

    @Override
    @Transactional
    public OrganizationResponseDTO rejectOrganization(Long id, String reason) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        org.setStatus(OrganizationStatus.REJECTED);
        Organization saved = organizationRepository.save(org);
        emailService.sendOrgApprovalEmail(org.getOrgEmail(), org.getOrgName(), false, reason);
        return toOrgDTO(saved);
    }

    @Override
    public List<OrganizationResponseDTO> getPendingOrganizations() {
        return organizationRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrganizationStatus.PENDING)
                .map(this::toOrgDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::toOrgDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(userId);
    }

    private OrganizationResponseDTO toOrgDTO(Organization org) {
        OrganizationResponseDTO dto = new OrganizationResponseDTO();
        dto.setId(org.getId());
        dto.setOrgName(org.getOrgName());
        dto.setOrgEmail(org.getOrgEmail());
        dto.setTaxId(org.getTaxId());
        dto.setLegalAddress(org.getLegalAddress());
        dto.setOrgDescription(org.getOrgDescription());
        dto.setStatus(org.getStatus());
        dto.setCreatedAt(org.getCreatedAt());
        return dto;
    }

    private UserResponseDTO toUserDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
