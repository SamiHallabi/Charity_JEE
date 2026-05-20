package com.ag.charity.controller;

import com.ag.charity.DTO.ParticipationResponseDTO;
import com.ag.charity.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping("/user/{userId}/action/{actionId}")
    public ResponseEntity<ParticipationResponseDTO> participate(
            @PathVariable Long userId,
            @PathVariable Long actionId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participationService.participate(userId, actionId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ParticipationResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(participationService.getParticipationsByUser(userId));
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<List<ParticipationResponseDTO>> getByAction(@PathVariable Long actionId) {
        return ResponseEntity.ok(participationService.getParticipationsByAction(actionId));
    }
}
