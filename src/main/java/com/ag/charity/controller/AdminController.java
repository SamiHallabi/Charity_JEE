package com.ag.charity.controller;

import com.ag.charity.DTO.AdminApprovalDTO;
import com.ag.charity.DTO.OrganizationResponseDTO;
import com.ag.charity.DTO.UserResponseDTO;
import com.ag.charity.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/organizations/pending")
    public ResponseEntity<List<OrganizationResponseDTO>> getPending() {
        return ResponseEntity.ok(adminService.getPendingOrganizations());
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrgs() {
        return ResponseEntity.ok(adminService.getAllOrganizations());
    }

    @PatchMapping("/organizations/{id}/approve")
    public ResponseEntity<OrganizationResponseDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveOrganization(id));
    }

    @PatchMapping("/organizations/{id}/reject")
    public ResponseEntity<OrganizationResponseDTO> reject(
            @PathVariable Long id,
            @RequestBody AdminApprovalDTO dto) {
        return ResponseEntity.ok(adminService.rejectOrganization(id, dto.getReason()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
