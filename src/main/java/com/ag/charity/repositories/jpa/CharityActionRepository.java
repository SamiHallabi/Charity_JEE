package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.enums.ActionStatus;
import com.ag.charity.entities.enums.Category;
import com.ag.charity.entities.jpa.CharityAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {

    List<CharityAction> findByCategory(Category category);

    List<CharityAction> findByStatus(ActionStatus status);

    List<CharityAction> findByCategoryAndStatus(Category category, ActionStatus status);

    List<CharityAction> findByOrganizationId(Long organizationId);

    List<CharityAction> findByOrganizationIdAndStatus(Long organizationId, ActionStatus status);

    @Query("SELECT a FROM CharityAction a WHERE " +
           "(:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR a.category = :category) " +
           "AND a.status = 'ACTIVE'")
    List<CharityAction> search(@Param("keyword") String keyword, @Param("category") Category category);

    List<CharityAction> findTop6ByStatusOrderByCreatedAtDesc(ActionStatus status);
}
