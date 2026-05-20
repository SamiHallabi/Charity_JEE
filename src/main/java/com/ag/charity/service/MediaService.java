package com.ag.charity.service;

import com.ag.charity.DTO.MediaResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    MediaResponseDTO uploadMedia(Long actionId, MultipartFile file);
    List<MediaResponseDTO> getMediaByAction(Long actionId);
    void deleteMedia(Long mediaId);
}
