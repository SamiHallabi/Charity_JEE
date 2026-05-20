package com.ag.charity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipationRequestDTO {

    @NotNull
    private Long charityActionId;
}
