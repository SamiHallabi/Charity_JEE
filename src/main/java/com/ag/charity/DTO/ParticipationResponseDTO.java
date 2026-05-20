package com.ag.charity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationResponseDTO {

    private Long id;
    private LocalDateTime registeredAt;
    private Long charityActionId;
    private String charityActionTitle;
    private Long userId;
    private String userFullName;
}
