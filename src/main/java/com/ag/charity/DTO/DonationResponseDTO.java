package com.ag.charity.DTO;

import com.ag.charity.entities.enums.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponseDTO {

    private Long id;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private DonationStatus status;
    private LocalDateTime donationDate;
    private Long charityActionId;
    private String charityActionTitle;
    private Long userId;
    private String userFullName;
}
