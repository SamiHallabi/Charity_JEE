package com.ag.charity.service;

import com.ag.charity.DTO.ParticipationResponseDTO;
import com.ag.charity.entities.jpa.CharityAction;
import com.ag.charity.entities.jpa.Participation;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.CharityActionRepository;
import com.ag.charity.repositories.jpa.ParticipationRepository;
import com.ag.charity.repositories.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final CharityActionRepository charityActionRepository;

    public ParticipationServiceImpl(ParticipationRepository participationRepository,
                                    UserRepository userRepository,
                                    CharityActionRepository charityActionRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.charityActionRepository = charityActionRepository;
    }

    @Override
    @Transactional
    public ParticipationResponseDTO participate(Long userId, Long charityActionId) {
        if (participationRepository.existsByUserIdAndCharityActionId(userId, charityActionId)) {
            throw new RuntimeException("Vous participez déjà à cette action");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        CharityAction action = charityActionRepository.findById(charityActionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));

        Participation participation = new Participation();
        participation.setUser(user);
        participation.setCharityAction(action);

        return toDTO(participationRepository.save(participation));
    }

    @Override
    public List<ParticipationResponseDTO> getParticipationsByUser(Long userId) {
        return participationRepository.findByUserIdOrderByRegisteredAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationResponseDTO> getParticipationsByAction(Long actionId) {
        return participationRepository.findByCharityActionId(actionId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean isParticipating(Long userId, Long actionId) {
        return participationRepository.existsByUserIdAndCharityActionId(userId, actionId);
    }

    private ParticipationResponseDTO toDTO(Participation p) {
        return ParticipationResponseDTO.builder()
                .id(p.getId())
                .registeredAt(p.getRegisteredAt())
                .charityActionId(p.getCharityAction().getId())
                .charityActionTitle(p.getCharityAction().getTitle())
                .userId(p.getUser().getId())
                .userFullName(p.getUser().getFirstName() + " " + p.getUser().getLastName())
                .build();
    }
}
