package com.ag.charity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateProfileDTO {

    private String firstName;
    private String lastName;
    private String password;
}
