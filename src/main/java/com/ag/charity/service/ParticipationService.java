package com.ag.charity.service;

import com.ag.charity.DTO.ParticipationResponseDTO;

import java.util.List;

public interface ParticipationService {
    ParticipationResponseDTO participate(Long userId, Long charityActionId);
    List<ParticipationResponseDTO> getParticipationsByUser(Long userId);
    List<ParticipationResponseDTO> getParticipationsByAction(Long actionId);
    boolean isParticipating(Long userId, Long actionId);
}
