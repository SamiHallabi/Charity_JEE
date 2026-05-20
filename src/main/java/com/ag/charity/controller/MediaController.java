package com.ag.charity.controller;

import com.ag.charity.DTO.MediaResponseDTO;
import com.ag.charity.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/action/{actionId}")
    public ResponseEntity<MediaResponseDTO> upload(
            @PathVariable Long actionId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mediaService.uploadMedia(actionId, file));
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<List<MediaResponseDTO>> getByAction(@PathVariable Long actionId) {
        return ResponseEntity.ok(mediaService.getMediaByAction(actionId));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> delete(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}
