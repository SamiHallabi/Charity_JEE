package com.ag.charity.service;

import com.ag.charity.DTO.RegisterRequestDTO;
import com.ag.charity.DTO.UpdateProfileDTO;
import com.ag.charity.DTO.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserResponseDTO register(RegisterRequestDTO dto);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateProfile(Long id, UpdateProfileDTO dto);
    void deleteUser(Long id);
    List<UserResponseDTO> getAllUsers();

}
