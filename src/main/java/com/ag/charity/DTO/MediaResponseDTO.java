package com.ag.charity.DTO;

import com.ag.charity.entities.enums.MediaFileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {

    private Long id;
    private String filename;
    private String url;
    private MediaFileType type;
    private Long charityActionId;
    private LocalDateTime uploadedAt;
}
