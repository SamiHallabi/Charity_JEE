package com.ag.charity.service;

import com.ag.charity.DTO.CharityActionCreateDTO;
import com.ag.charity.DTO.CharityActionResponseDTO;
import com.ag.charity.DTO.CharityActionUpdateDTO;
import com.ag.charity.entities.enums.Category;

import java.util.List;

public interface CharityActionService {
    CharityActionResponseDTO createAction(Long organizationId, CharityActionCreateDTO dto);
    CharityActionResponseDTO updateAction(Long actionId, CharityActionUpdateDTO dto);
    void archiveAction(Long actionId);
    CharityActionResponseDTO getActionById(Long actionId);
    List<CharityActionResponseDTO> getAllActiveActions();
    List<CharityActionResponseDTO> getActionsByCategory(Category category);
    List<CharityActionResponseDTO> getActionsByOrganization(Long organizationId);
    List<CharityActionResponseDTO> searchActions(String keyword, Category category);
    List<CharityActionResponseDTO> getFeaturedActions();
}
