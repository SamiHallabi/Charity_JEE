package com.ag.charity.controller;


import com.ag.charity.DTO.*;
import com.ag.charity.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/register")
    public ResponseEntity<OrganizationResponseDTO> register(@RequestBody OrganizationRegisterRequestDTO dto){
        OrganizationResponseDTO response = organizationService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(@PathVariable Long id){
        OrganizationResponseDTO response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateProfile(@PathVariable Long id, @RequestBody OrganizationProfileUpdateDTO dto) {
        OrganizationResponseDTO response = organizationService.updateOrganizationProfile(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());

}
    }


