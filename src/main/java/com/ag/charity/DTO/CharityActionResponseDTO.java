package com.ag.charity.DTO;

import com.ag.charity.entities.enums.ActionStatus;
import com.ag.charity.entities.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharityActionResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate eventDate;
    private String location;
    private BigDecimal fundraisingGoal;
    private BigDecimal amountCollected;
    private Integer progressPercent;
    private ActionStatus status;
    private Category category;
    private Long organizationId;
    private String organizationName;
    private String organizationLogo;
    private LocalDateTime createdAt;
}
