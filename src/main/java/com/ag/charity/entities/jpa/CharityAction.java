package com.ag.charity.entities.jpa;

import com.ag.charity.entities.enums.ActionStatus;
import com.ag.charity.entities.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "charity_actions")
public class CharityAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate eventDate;

    private String location;

    @Column(precision = 10, scale = 2)
    private BigDecimal fundraisingGoal;

    @Column(precision = 10, scale = 2)
    private BigDecimal amountCollected = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionStatus status = ActionStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Media> medias = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donation> donations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participation> participations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = ActionStatus.ACTIVE;
        if (this.amountCollected == null) this.amountCollected = BigDecimal.ZERO;
    }
}
