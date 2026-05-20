package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.jpa.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByUserIdOrderByDonationDateDesc(Long userId);

    List<Donation> findByCharityActionIdOrderByDonationDateDesc(Long charityActionId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.charityAction.id = :actionId AND d.status = 'COMPLETED'")
    BigDecimal sumCompletedAmountByActionId(@Param("actionId") Long actionId);
}
