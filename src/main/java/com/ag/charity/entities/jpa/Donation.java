package com.ag.charity.entities.jpa;

import com.ag.charity.entities.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_action_id", nullable = false)
    private CharityAction charityAction;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String paymentMethod;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime donationDate;

    @PrePersist
    protected void onCreate() {
        this.donationDate = LocalDateTime.now();
        if (this.status == null) this.status = DonationStatus.PENDING;
    }
}
