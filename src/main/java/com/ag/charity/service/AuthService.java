package com.ag.charity.service;

import com.ag.charity.DTO.LoginRequestDTO;
import com.ag.charity.DTO.UserResponseDTO;

public interface AuthService {
    UserResponseDTO authenticate(LoginRequestDTO dto);
    UserResponseDTO getCurrentUser(String email);
}
