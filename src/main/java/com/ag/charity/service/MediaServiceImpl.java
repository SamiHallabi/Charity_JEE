package com.ag.charity.service;

import com.ag.charity.DTO.MediaResponseDTO;
import com.ag.charity.entities.enums.MediaFileType;
import com.ag.charity.entities.jpa.CharityAction;
import com.ag.charity.entities.jpa.Media;
import com.ag.charity.repositories.jpa.CharityActionRepository;
import com.ag.charity.repositories.jpa.MediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final CharityActionRepository charityActionRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            CharityActionRepository charityActionRepository) {
        this.mediaRepository = mediaRepository;
        this.charityActionRepository = charityActionRepository;
    }

    @Override
    public MediaResponseDTO uploadMedia(Long actionId, MultipartFile file) {
        CharityAction action = charityActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID() + ext;

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Échec du téléchargement du fichier : " + e.getMessage());
        }

        String contentType = file.getContentType() != null ? file.getContentType() : "";
        MediaFileType type = contentType.startsWith("video") ? MediaFileType.VIDEO : MediaFileType.IMAGE;

        Media media = new Media();
        media.setFilename(filename);
        media.setUrl("/uploads/" + filename);
        media.setType(type);
        media.setCharityAction(action);

        return toDTO(mediaRepository.save(media));
    }

    @Override
    public List<MediaResponseDTO> getMediaByAction(Long actionId) {
        return mediaRepository.findByCharityActionId(actionId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteMedia(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Média non trouvé"));
        try {
            Path file = Paths.get(uploadDir).resolve(media.getFilename());
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
        }
        mediaRepository.deleteById(mediaId);
    }

    private MediaResponseDTO toDTO(Media m) {
        return MediaResponseDTO.builder()
                .id(m.getId())
                .filename(m.getFilename())
                .url(m.getUrl())
                .type(m.getType())
                .charityActionId(m.getCharityAction().getId())
                .uploadedAt(m.getUploadedAt())
                .build();
    }
}
