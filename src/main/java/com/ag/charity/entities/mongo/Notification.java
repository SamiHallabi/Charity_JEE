package com.ag.charity.entities.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String recipientEmail;

    private String type;

    private String subject;

    private String message;

    private boolean read = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
