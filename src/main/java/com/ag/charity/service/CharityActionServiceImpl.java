package com.ag.charity.service;

import com.ag.charity.DTO.CharityActionCreateDTO;
import com.ag.charity.DTO.CharityActionResponseDTO;
import com.ag.charity.DTO.CharityActionUpdateDTO;
import com.ag.charity.entities.enums.ActionStatus;
import com.ag.charity.entities.enums.Category;
import com.ag.charity.entities.jpa.CharityAction;
import com.ag.charity.entities.jpa.Organization;
import com.ag.charity.repositories.jpa.CharityActionRepository;
import com.ag.charity.repositories.jpa.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharityActionServiceImpl implements CharityActionService {

    private final CharityActionRepository charityActionRepository;
    private final OrganizationRepository organizationRepository;

    public CharityActionServiceImpl(CharityActionRepository charityActionRepository,
                                    OrganizationRepository organizationRepository) {
        this.charityActionRepository = charityActionRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional
    public CharityActionResponseDTO createAction(Long organizationId, CharityActionCreateDTO dto) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));

        CharityAction action = new CharityAction();
        action.setTitle(dto.getTitle());
        action.setDescription(dto.getDescription());
        action.setEventDate(dto.getEventDate());
        action.setLocation(dto.getLocation());
        action.setFundraisingGoal(dto.getFundraisingGoal());
        action.setCategory(dto.getCategory());
        action.setStatus(ActionStatus.ACTIVE);
        action.setOrganization(org);

        return toDTO(charityActionRepository.save(action));
    }

    @Override
    @Transactional
    public CharityActionResponseDTO updateAction(Long actionId, CharityActionUpdateDTO dto) {
        CharityAction action = charityActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));

        if (dto.getTitle() != null) action.setTitle(dto.getTitle());
        if (dto.getDescription() != null) action.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) action.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) action.setLocation(dto.getLocation());
        if (dto.getFundraisingGoal() != null) action.setFundraisingGoal(dto.getFundraisingGoal());
        if (dto.getCategory() != null) action.setCategory(dto.getCategory());

        return toDTO(charityActionRepository.save(action));
    }

    @Override
    @Transactional
    public void archiveAction(Long actionId) {
        CharityAction action = charityActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        action.setStatus(ActionStatus.ARCHIVED);
        charityActionRepository.save(action);
    }

    @Override
    public CharityActionResponseDTO getActionById(Long actionId) {
        CharityAction action = charityActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        return toDTO(action);
    }

    @Override
    public List<CharityActionResponseDTO> getAllActiveActions() {
        return charityActionRepository.findByStatus(ActionStatus.ACTIVE)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CharityActionResponseDTO> getActionsByCategory(Category category) {
        return charityActionRepository.findByCategoryAndStatus(category, ActionStatus.ACTIVE)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CharityActionResponseDTO> getActionsByOrganization(Long organizationId) {
        return charityActionRepository.findByOrganizationId(organizationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CharityActionResponseDTO> searchActions(String keyword, Category category) {
        return charityActionRepository.search(keyword, category)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CharityActionResponseDTO> getFeaturedActions() {
        return charityActionRepository.findTop6ByStatusOrderByCreatedAtDesc(ActionStatus.ACTIVE)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CharityActionResponseDTO toDTO(CharityAction action) {
        int progress = 0;
        if (action.getFundraisingGoal() != null &&
                action.getFundraisingGoal().compareTo(BigDecimal.ZERO) > 0 &&
                action.getAmountCollected() != null) {
            progress = action.getAmountCollected()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(action.getFundraisingGoal(), 0, RoundingMode.HALF_UP)
                    .intValue();
            progress = Math.min(progress, 100);
        }

        return CharityActionResponseDTO.builder()
                .id(action.getId())
                .title(action.getTitle())
                .description(action.getDescription())
                .eventDate(action.getEventDate())
                .location(action.getLocation())
                .fundraisingGoal(action.getFundraisingGoal())
                .amountCollected(action.getAmountCollected())
                .progressPercent(progress)
                .status(action.getStatus())
                .category(action.getCategory())
                .organizationId(action.getOrganization().getId())
                .organizationName(action.getOrganization().getOrgName())
                .organizationLogo(action.getOrganization().getLogoUrl())
                .createdAt(action.getCreatedAt())
                .build();
    }
}
