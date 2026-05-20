package com.ag.charity.controller;

import com.ag.charity.DTO.DonationRequestDTO;
import com.ag.charity.DTO.DonationResponseDTO;
import com.ag.charity.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<DonationResponseDTO> donate(
            @PathVariable Long userId,
            @Valid @RequestBody DonationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(donationService.donate(userId, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DonationResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(donationService.getDonationsByUser(userId));
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<List<DonationResponseDTO>> getByAction(@PathVariable Long actionId) {
        return ResponseEntity.ok(donationService.getDonationsByAction(actionId));
    }
}
