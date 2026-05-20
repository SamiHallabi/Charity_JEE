package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.jpa.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByUserIdOrderByRegisteredAtDesc(Long userId);

    List<Participation> findByCharityActionId(Long charityActionId);

    boolean existsByUserIdAndCharityActionId(Long userId, Long charityActionId);

    long countByCharityActionId(Long charityActionId);
}
