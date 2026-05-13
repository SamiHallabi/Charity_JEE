package com.ag.charity.repositories.jpa;

import com.ag.charity.entities.jpa.Organization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Organization findByOrgEmail(String orgEmail);
    boolean existsByOrgEmail(@Email(message = "Please provide a valid email address") @NotBlank(message = "Email is required") String email);
}