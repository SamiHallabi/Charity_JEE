package com.ag.charity.controller;

import com.ag.charity.DTO.CharityActionCreateDTO;
import com.ag.charity.DTO.CharityActionResponseDTO;
import com.ag.charity.DTO.CharityActionUpdateDTO;
import com.ag.charity.entities.enums.Category;
import com.ag.charity.service.CharityActionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class CharityActionController {

    private final CharityActionService charityActionService;

    @GetMapping
    public ResponseEntity<List<CharityActionResponseDTO>> getAllActive() {
        return ResponseEntity.ok(charityActionService.getAllActiveActions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharityActionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(charityActionService.getActionById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CharityActionResponseDTO>> getByCategory(
            @PathVariable Category category) {
        return ResponseEntity.ok(charityActionService.getActionsByCategory(category));
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<CharityActionResponseDTO>> getByOrganization(
            @PathVariable Long orgId) {
        return ResponseEntity.ok(charityActionService.getActionsByOrganization(orgId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CharityActionResponseDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category) {
        return ResponseEntity.ok(charityActionService.searchActions(keyword, category));
    }

    @PostMapping("/organization/{orgId}")
    public ResponseEntity<CharityActionResponseDTO> create(
            @PathVariable Long orgId,
            @Valid @RequestBody CharityActionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(charityActionService.createAction(orgId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CharityActionResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CharityActionUpdateDTO dto) {
        return ResponseEntity.ok(charityActionService.updateAction(id, dto));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable Long id) {
        charityActionService.archiveAction(id);
        return ResponseEntity.noContent().build();
    }
}
