package com.ag.charity.entities.jpa;

import com.ag.charity.entities.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String orgEmail;

        @Column(nullable = false)
        private String orgName;

        @Column(nullable = false)
        private String orgPassword;

        @Column(nullable = false)
        private String taxId;

        @Column(nullable = false)
        private String legalAddress;

        private String orgDescription;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrganizationStatus status = OrganizationStatus.PENDING;  // ← enum pour "PENDING", "APPROVED", "REJECTED"


        @Column(updatable = false)
        private LocalDateTime createdAt;  // ← nom cohérent

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();  // ← auto-rempli à la création
        }
}
