package com.ag.charity.entities.jpa;

import com.ag.charity.entities.enums.OrganizationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @Column(columnDefinition = "TEXT")
    private String orgDescription;

    private String logoUrl;

    private String contactPerson;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationStatus status = OrganizationStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CharityAction> charityActions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = OrganizationStatus.PENDING;
    }
}
