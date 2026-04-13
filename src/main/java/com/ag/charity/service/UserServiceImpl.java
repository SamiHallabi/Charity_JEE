package com.ag.charity.service;

import com.ag.charity.DTO.RegisterRequestDTO;
import com.ag.charity.DTO.UpdateProfileDTO;
import com.ag.charity.DTO.UserResponseDTO;
import com.ag.charity.entities.enums.Role;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return toDTO(user);
    }

    @Override
    public UserResponseDTO updateProfile(Long id, UpdateProfileDTO dto) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(dto.getLastName());
            }
            if (dto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        return toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(id);

    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt() != null ? LocalDateTime.parse(user.getCreatedAt().toString()) : null);
        return dto;
    }
}
