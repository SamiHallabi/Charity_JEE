package com.ag.charity.DTO;

import com.ag.charity.entities.enums.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CharityActionCreateDTO {

    @NotBlank
    private String title;

    private String description;

    private LocalDate eventDate;

    private String location;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal fundraisingGoal;

    @NotNull
    private Category category;
}
