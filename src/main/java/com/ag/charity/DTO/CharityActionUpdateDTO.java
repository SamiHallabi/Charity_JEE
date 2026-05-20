package com.ag.charity.DTO;

import com.ag.charity.entities.enums.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CharityActionUpdateDTO {

    private String title;

    private String description;

    private LocalDate eventDate;

    private String location;

    private BigDecimal fundraisingGoal;

    private Category category;
}
